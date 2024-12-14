package com.example.ozzy.emotioncard.service;

import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.diary.dto.request.Message;
import com.example.ozzy.diary.dto.request.MessageRequest;
import com.example.ozzy.diary.entity.Diary;
import com.example.ozzy.emotioncard.dto.response.EmotionAnalysisResponse;
import com.example.ozzy.emotioncard.entity.EmotionCard;
import com.example.ozzy.emotioncard.mapper.EmotionCardMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmotionCardService {

    private final EmotionCardMapper emotionCardMapper;
    private final RestTemplate restTemplate;

    private final String HOST = "https://clovastudio.apigw.ntruss.com/serviceapp/v1/chat-completions/HCX-003";

    @Value("${clova-studio.api-key}")
    private String API_KEY;

    @Value("${clova-studio.apigw-api-key}")
    private String APIGW_API_KEY;

    @Value("${clova-studio.request-id}")
    private String REQUEST_ID;


    private static final String SYSTEM_MESSAGE_SPLIT = "- 일기 답변 및 문장 나누기 시스템입니다.\n- 사용자가 자신의 일기를 입력하면, 일기에 대한 답변과 일기를 문장 단위로 나누어 이를 json 형식으로 응답합니다.\n- 일기에 대한 답변은 사용자의 일기 내용을 파악하여, 적절한 공감이 담아 4문장 분량으로 표현합니다. \n- 문장 나누기는 배열로 하여 각 문장이 배열에 들어가는 형태이며, 마침표로 구분될 때 문장이 너무 짧다면 앞이나 뒷 문장과 합쳐진 문장으로 분리합니다.\n- 답변은 json 형식으로 출력합니다.\n- 아래 예시를 참고합니다. 답변은 항상 아래 예시와 같은 포맷으로 나와야합니다.\n\n###\n사용자: 어제 새벽에 유튜브를 보느라 늦게 자버렸다. 그래서 오늘 오후 4시까지 늦잠을 잤다. 오늘은 한진 역량검사를 봐야해서 마음이 약간 조급했지만, 일단 밥을 먹고 역량검사를 준비했다. 역량검사는 원래 보던 유형이 아니라 약간 당황했지만, 그래도 어느 정도 잘 본 것 같다. 그리고 내일은 다음 주 면접 때 입고 갈 정장을 대여하러 가야한다. 우하하 설렌다.\\n어시스턴트: \\n{\\n  \\\"reply\\\": \\\"어제 늦게 잔 만큼 오늘 늦잠을 자서 하루가 조금 흐트러진 느낌이 들었겠어요. 하지만 중요한 역량검사를 준비하고 잘 마친 당신의 모습에서 집중력과 책임감이 느껴져요. 내일은 면접 준비로 정장을 대여하러 가는 설렘도 있으니, 좋은 일이 더 많이 이어질 것 같아요!\\\",\\n  \\\"\\bsentences\\\": [\\\"어제 새벽에 유튜브를 보느라 늦게 자버렸다.\\\", \\\"그래서 오늘 오후 4시까지 늦잠을 잤다.\\\", \\\"오늘은 한진 역량검사를 봐야 해서 마음이 약간 조급했지만, 일단 밥을 먹고 역량검사를 준비했다.\\\", \\\"역량검사는 원래 보던 유형이 아니라 약간 당황했지만, 그래도 어느 정도 잘 본 것 같다.\\\", \\\"그리고 내일은 다음 주 면접 때 입고 갈 정장을 대여하러 가야 한다.\\\", \\\"우하하 설렌다.\\\"]\\n}\"";
    private static final String SYSTEM_MESSAGE_EMOTION = "- 사용자가 문장을 입력하면 감정 분석 결과를 응답합니다. \n- 감정 분석은 사용자의 문장에 대해 진행합니다. 문장의 감정은 {\"기쁨\", \"슬픔\", \"놀람\", \"분노\", \"공포\", \"나쁨\", \"중립\"} 7가지 중 하나로 분류합니다. 반드시 7개의 감정 중 하나로만 분류합니다. 분류 시 softmax 함수를 활용하며, 임계값은 0.4로 지정합니다. 모든 감정 확률이 임계값 이하라면, \"중립\"으로 처리합니다. \n- 아래 예시를 참고합니다. 답변은 항상 아래 예시와 같은 포맷으로 나와야합니다.\n\n###\n사용자: 어제 새벽에 유튜브를 보느라 늦게 자버렸다.\noutput: 중립\n\n사용자: 그래서 오늘 오후 4시까지 늦잠을 잤다.\noutput: 중립\n\n사용자: 오늘은 한진 역량검사를 봐야 해서 마음이 약간 조급했지만, 일단 밥을 먹고 역량검사를 준비했다.\noutput: 공포\n\n사용자: 역량검사는 원래 보던 유형이 아니라 약간 당황했지만, 그래도 어느 정도 잘 본 것 같다.\noutput: 기쁨\n\n사용자: 그리고 내일은 다음 주 면접 때 입고 갈 정장을 대여하러 가야 한다.\noutput: 중립\n\n사용자: 흑흑 슬프다.\noutput: 슬픔.";

    public int initEmotionCard() {
        EmotionCard emotionCard = new EmotionCard();
        emotionCard.setIsAnalyzed("N");
        emotionCard.setReply("");

        emotionCardMapper.saveEmotionCard(emotionCard);

        return emotionCard.getEmotionCardSeq();
    }

    public EmotionAnalysisResponse getEmotionAnalysis(int emotionCardSeq) {
        EmotionCard emotionCard = emotionCardMapper.getEmotionCardBySeq(emotionCardSeq);

        if(emotionCard == null) {
            throw new CommonException("감정카드가 없습니다.", 404);
        }
        if("N".equals(emotionCard.getIsAnalyzed())) {
            throw new CommonException("감정 분석이 완료되지 않았습니다.", 400);
        }

        Map<String, Integer> emotionCounts = new HashMap<>();
        emotionCounts.put("happy", emotionCard.getHappy());
        emotionCounts.put("sad", emotionCard.getSad());
        emotionCounts.put("surprised", emotionCard.getSurprised());
        emotionCounts.put("angry", emotionCard.getAngry());
        emotionCounts.put("fearful", emotionCard.getFearful());
        emotionCounts.put("bad", emotionCard.getDisgusted());

        int totalSentences = calculateTotalSentences(emotionCounts) + emotionCard.getNeutrality();

        Map<String, Double> emotionPercentages = new HashMap<>();
        for(Map.Entry<String, Integer> entry : emotionCounts.entrySet()) {
            if(entry.getValue() > 0) {
                double percentage = (double) entry.getValue() / totalSentences * 100;
                emotionPercentages.put(entry.getKey(), percentage);
            }
        }

        List<Map.Entry<String, Double>> topEmotions = getTopEmotions(emotionPercentages);

        EmotionAnalysisResponse response = new EmotionAnalysisResponse();
        response.setReply(emotionCard.getReply());
        response.setEmotionPercentages(topEmotions);

        return response;
    }

    private int calculateTotalSentences(Map<String, Integer> emotionCounts) {
        int total = 0;
        for (int count : emotionCounts.values()) {
            total += count; // 모든 감정의 수 합산
        }
        return total;
    }

    private List<Map.Entry<String, Double>> getTopEmotions(Map<String, Double> emotions) {
        // 감정 비율을 내림차순으로 정렬
        List<Map.Entry<String, Double>> sortedEmotions = emotions.entrySet().stream()
                .filter(e -> e.getValue() > 0) // 0보다 큰 감정만 필터링
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // 내림차순 정렬
                .toList();

        List<Map.Entry<String, Double>> topEmotions = new ArrayList<>();

        if (!sortedEmotions.isEmpty()) {
            // 상위 3개 감정을 추가
            double highestValue = sortedEmotions.get(0).getValue();
            int count = 0;

            for (Map.Entry<String, Double> entry : sortedEmotions) {
                if (count < 3) {
                    topEmotions.add(entry);
                    count++;
                } else if (entry.getValue() == highestValue) {
                    // 동순위가 발생하면 계속 추가
                    topEmotions.add(entry);
                } else {
                    break; // 더 이상 추가하지 않음
                }
            }
        }

        return topEmotions;
    }

    public void analyzeDiary(Diary diary) throws JsonProcessingException {

        String userDiary = diary.getContent();
        String splitResult = getReplyAndSplitSentences(userDiary);
        JsonNode contentNode = parseJsonResponse(splitResult);

        String reply = contentNode.path("reply").asText();
        List<String> sentences = getSentencesFromNode(contentNode.path("sentences"));

        Map<String, Integer> emotionCounts = initializeEmotionCounts();
        analyzeSentenceEmotions(sentences, emotionCounts);

        updateEmotionCard(diary.getEmotionCardSeq(), reply, emotionCounts);
    }

    private void updateEmotionCard(int emotionCardSeq, String reply, Map<String, Integer> emotionCounts) {

        EmotionCard emotionCard = new EmotionCard();
        emotionCard.setEmotionCardSeq(emotionCardSeq);
        emotionCard.setIsAnalyzed("Y");
        emotionCard.setReply(reply);
        emotionCard.setHappy(emotionCounts.get("기쁨"));
        emotionCard.setSad(emotionCounts.get("슬픔"));
        emotionCard.setSurprised(emotionCounts.get("놀람"));
        emotionCard.setAngry(emotionCounts.get("분노"));
        emotionCard.setFearful(emotionCounts.get("공포"));
        emotionCard.setDisgusted(emotionCounts.get("나쁨"));
        emotionCard.setNeutrality(emotionCounts.get("중립"));

        // EMOTION_CARD 업데이트
        emotionCardMapper.updateEmotionCard(emotionCard);
    }

    private String getReplyAndSplitSentences(String userDiary) {
        return sendRequest(createMessageRequest(SYSTEM_MESSAGE_SPLIT, userDiary, 0.6, 7.29));
    }

    private String getSentenceEmotion(String sentence) {
        return sendRequest(createMessageRequest(SYSTEM_MESSAGE_EMOTION, sentence, 0.1, 1.2));
    }

    private String sendRequest(MessageRequest messageRequest) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<MessageRequest> entity = new HttpEntity<>(messageRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(HOST, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("EmotionCardService.sendRequest");
            System.out.println("e = " + e);
        }
        System.out.println("EmotionCardService.sendRequest 여기냐!!");
        return "일기 ai 답변이니?";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", API_KEY);
        headers.set("X-NCP-APIGW-API-KEY", APIGW_API_KEY);
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", REQUEST_ID);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    private MessageRequest createMessageRequest(String systemMessageContent, String userMessageContent, double temperature, double repeatPenalty) {
        MessageRequest requestBody = new MessageRequest();

        Message systemMessage = new Message();
        systemMessage.setRole("system");
        systemMessage.setContent(systemMessageContent);

        Message userMessage = new Message();
        userMessage.setRole("user");
        userMessage.setContent(userMessageContent);

        requestBody.getMessages().add(systemMessage);
        requestBody.getMessages().add(userMessage);

        requestBody.setTopP(0.6);
        requestBody.setTopK(0);
        requestBody.setMaxTokens(1500);
        requestBody.setTemperature(temperature);
        requestBody.setRepeatPenalty(repeatPenalty);
        requestBody.setIncludeAiFilters(true);

        return requestBody;
    }

    private JsonNode parseJsonResponse(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode messageNode = rootNode.path("result").path("message").path("content");
        return objectMapper.readTree(messageNode.asText());
    }

    private List<String> getSentencesFromNode(JsonNode sentencesNode) {
        List<String> sentences = new ArrayList<>();
        sentencesNode.forEach(sentence -> sentences.add(sentence.asText()));
        return sentences;
    }

    private Map<String, Integer> initializeEmotionCounts() {
        Map<String, Integer> emotionCounts = new HashMap<>();
        emotionCounts.put("기쁨", 0);
        emotionCounts.put("슬픔", 0);
        emotionCounts.put("놀람", 0);
        emotionCounts.put("분노", 0);
        emotionCounts.put("공포", 0);
        emotionCounts.put("나쁨", 0);
        emotionCounts.put("중립", 0);
        return emotionCounts;
    }

    private void analyzeSentenceEmotions(List<String> sentences, Map<String, Integer> emotionCounts) throws JsonProcessingException {
        for (String sentence : sentences) {
            String emotionResult = getSentenceEmotion(sentence);
            String emotion = extractEmotion(emotionResult);
            emotionCounts.put(emotion, emotionCounts.get(emotion) + 1);
        }
    }

    private String extractEmotion(String emotionResult) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode emotionNode = objectMapper.readTree(emotionResult);
        String emotionContent = emotionNode.path("result").path("message").path("content").asText().trim();
        return emotionContent.substring(emotionContent.indexOf(":") + 1).trim();
    }
}
