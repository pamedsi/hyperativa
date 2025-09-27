package com.hyperativa.card.application.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.hyperativa.card.application.api.CardNumber;
import com.hyperativa.card.application.repository.BatchRepository;
import com.hyperativa.card.domain.Batch;
import com.hyperativa.card.domain.Card;
import com.hyperativa.card.application.repository.CardRepository;
import com.hyperativa.card.infra.CardNumberEncryptor;
import com.hyperativa.card.infra.ValidCardNumber;
import com.hyperativa.handler.exceptions.APIException;
import com.hyperativa.handler.exceptions.InvalidFileException;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import com.hyperativa.utils.Hasher;
import com.hyperativa.card.application.api.CardId;


@Log4j2
@Service
@RequiredArgsConstructor
public class CardApplicationService implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final Hasher hasher;
    private final CardNumberEncryptor encryptor;
    private final Validator validator;
    private static final int MAX_HEADER_LENGTH = 51;

    @Override
    public void addSingleCard(CardNumber cardNumber, String ownerCardEmail) {
        log.info("[starts]: CardApplicationService.addSingleCard()");
        User owner = userRepository.getUserByEmail(ownerCardEmail);
        String hashCardNumber = hasher.hashCardNumber(cardNumber.cardNumber());
        String encryptedCardNumber = encryptor.encrypt(cardNumber.cardNumber());
        Card newCard = new Card(owner, hashCardNumber, encryptedCardNumber);
        cardRepository.save(newCard);
        log.info("[ends]: CardApplicationService.addSingleCard()");
    }

    @Override
    public void addBatchCards(MultipartFile file, String ownerCardEmail) {
        log.info("[starts]: CardApplicationService.addBatchCards()");

        // Initiating variables
        User owner = userRepository.getUserByEmail(ownerCardEmail);
        String[] lines = extractFileLines(file);
        BatchHeader batchHeader = extractHeaderMetadata(lines[0], file);
        List<Card> cards = new ArrayList<>();

        // Processing lines
        ProcessingResult result = processCardLines(lines, owner, cards);
        validateAmountOfCards(batchHeader.amount(), cards.size(), result.successCount(), result.failureCount());

        // Persisting data
        Batch batch = new Batch(batchHeader, owner);
        batch = batchRepository.saveAndGet(batch);
        saveBatchAndCards(cards, batch, result.successCount(), result.failureCount());
        log.info("[ends]: CardApplicationService.addBatchCards()");
    }

    @Override
    public CardId getCardInfo(String cardNumber, String ownerEmail) {
        log.info("[starts]: CardApplicationService.getCardInfo()");
        User owner = userRepository.getUserByEmail(ownerEmail);
        String hashCardNumber = hasher.hashCardNumber(cardNumber);
        Card card = cardRepository.getCardByOwner(owner, hashCardNumber);
        log.info("[ends]: CardApplicationService.getCardInfo()");
        return new CardId(card.getIdentifier());
    }

    private String[] extractFileLines(MultipartFile file) {
        log.debug("[starts]: CardApplicationService.splitInLines()");
        String[] lines;
        try {
            byte[] bytes = file.getBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);
            lines = content.split("\n");
            if (lines.length < 3) {
                throw new InvalidFileException("File is invalid. It must have at least 3 lines.");
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new InvalidFileException();
        }
        log.debug("[ends]: CardApplicationService.splitInLines()");
        return lines;
    }

    private BatchHeader extractHeaderMetadata(String line, MultipartFile file) {
        log.debug("[starts]: CardApplicationService.processHeader()");
        if (line.length() > MAX_HEADER_LENGTH) {
            throw new InvalidFileException("Header line is too long. Max 51 characters.");
        }
        String paddedLine = String.format("%-51s", line);
        String name = substringSafe(paddedLine, 0, 29).trim();
        String date = substringSafe(paddedLine, 29, 37).trim();
        String batch = substringSafe(paddedLine, 37, 45).trim();
        String count = substringSafe(paddedLine, 45, 51).trim();
        BatchHeader batchHeader;

        try {
            int amount = Integer.parseInt(count);
            String originalFilename = file.getOriginalFilename();
            batchHeader = new BatchHeader(name, date, batch, amount, originalFilename);
        } catch (NumberFormatException e) {
            throw new InvalidFileException("Invalid amount on the header %s: " + count);
        }
        log.debug("[ends]: CardApplicationService.processHeader()");
        return batchHeader;
    }

    private String substringSafe(String str, int start, int end) {
        if (str == null) return "";
        if (start > str.length()) return "";
        if (end > str.length()) return str.substring(start);
        return str.substring(start, end);
    }

    private ProcessingResult processCardLines(String[] lines, User owner, List<Card> cards) {
        int successful = 0;
        int failed = 0;
        for (int i = 1; i < lines.length - 1; i++) {
            if (!lines[i].trim().isEmpty()) {
                CardInfo card = extractCardInfo(lines[i], i);
                if (card != null) {
                    boolean worked = buildCardEntity(card, owner, cards, i);
                    if (worked) successful++;
                    else failed++;
                }
            }
        }
        return new ProcessingResult(successful, failed);
    }

    private CardInfo extractCardInfo(String line, int lineNumber) {
        String trimmedLine = line.trim();
        if (trimmedLine.isEmpty()) return null;

        String paddedLine = String.format("%-26s", trimmedLine);

        if (paddedLine.charAt(0) != 'C') {
            log.warn("Line {} ignored - not a card line: {}", lineNumber, trimmedLine);
            return null;
        }

        String batchNumber = substringSafe(paddedLine, 1, 7).trim();
        String cardNumber = substringSafe(paddedLine, 7, 26).trim();

        Set<ConstraintViolation<ValidCardNumber>> violations = validator.validateValue(
                ValidCardNumber.class, "cardNumber", cardNumber
        );

        if (!violations.isEmpty()) {
            String errorMessage = violations.iterator().next().getMessage();
            log.warn("Invalid card number at line {}: {} - {}", lineNumber, cardNumber, errorMessage);
            return null;
        }

        return new CardInfo(batchNumber, cardNumber, lineNumber);
    }

    private boolean buildCardEntity(CardInfo cardInfo, User owner, List<Card> cards, int index) {
        try {
            String hashCardNumber = hasher.hashCardNumber(cardInfo.cardNumber());
            String encryptedCardNumber = encryptor.encrypt(cardInfo.cardNumber());
            Card newCard = new Card(owner, hashCardNumber, encryptedCardNumber);
            cards.add(newCard);
            return true;
        } catch (Exception e) {
            log.error("Error processing card at line {}: {}", index, e.getMessage());
            return false;
        }
    }

    private void saveBatchAndCards(List<Card> cards, Batch batch, int successful, int failed) {
        try {
            cards.forEach(card -> card.setBatch(batch));
            cardRepository.saveAll(cards);
            if (failed == 0) {
                batch.markAsCompleted();
            } else if (successful == 0) {
                batch.markAsFailed();
            } else {
                batch.markAsPartialSuccess(successful, failed);
            }
            batchRepository.save(batch);
        } catch (APIException e) {
            batch.markAsFailed();
            batch.setErrorMessage(e.getMessage());
            batchRepository.save(batch);
            throw e;
        }
    }

    private void validateAmountOfCards(int expected, int actual, int successful, int failed) {
        if (expected != actual) {
            throw new InvalidFileException(String.format(
                    "Processed %d cards but header specified %d. Successful: %d, Failed: %d",
                    expected, actual, successful, failed)
            );
        }
    }

}
