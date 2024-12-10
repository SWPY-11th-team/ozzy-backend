package com.example.ozzy.diary.service;

import com.example.ozzy.common.utils.ObjectMapperUtil;
import com.example.ozzy.diary.dto.request.Message;
import com.example.ozzy.diary.dto.request.MessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final RestTemplate restTemplate;
    private final ObjectMapperUtil objectMapper;

    public String split() {

        final String url = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-003";

        // ai 설정
        MessageRequest requestBody = new MessageRequest();
        requestBody.setTopP(0.6);
        requestBody.setTopK(0);
        requestBody.setMaxTokens(1500);
        requestBody.setTemperature(0.6);
        requestBody.setRepeatPenalty(7.29);
        requestBody.setIncludeAiFilters(true);
        requestBody.setSeed(0);

        // system
        List<Message> messages = new ArrayList<>();
        Message systemMessage = new Message();
        systemMessage.setRole("system");
        systemMessage.setContent("일기 답변 및 문장 나누기 시스템입니다. 사용자가 자신의 일기를 입력하면, 일기에 대한 답변과 일기를 문장 단위로 나누어 이를 json 형식으로 응답합니다. 일기에 대한 답변은 사용자의 일기 내용을 파악하여, 적절한 공감이 담아 4문장 분량으로 표현합니다. 문장 나누기는 배열로 하여 각 문장이 배열에 들어가는 형태이며, 마침표로 구분될 때 문장이 너무 짧다면 앞이나 뒷 문장과 합쳐진 문장으로 분리합니다. 답변은 json 형식으로 출력합니다. 아래 예시를 참고합니다. 답변은 항상 아래 예시와 같은 포맷으로 나와야합니다.");
        messages.add(systemMessage);

        // user 일기
        Message userMessage = new Message();
        userMessage.setRole("user");
        userMessage.setContent("어제 새벽에 유튜브를 보느라 늦게 자버렸다. 그래서 오늘 오후 4시까지 늦잠을 잤다. 오늘은 한진 역량검사를 봐야해서 마음이 약간 조급했지만, 일단 밥을 먹고 역량검사를 준비했다. 역량검사는 원래 보던 유형이 아니라 약간 당황했지만, 그래도 어느 정도 잘 본 것 같다. 그리고 내일은 다음 주 면접 때 입고 갈 정장을 대여하러 가야한다. 우하하 설렌다.");
        messages.add(userMessage);

        // 메시지 설정
        requestBody.setMessages(messages);

        String str = objectMapper.writeValueAsString(requestBody);
        System.out.println("str = " + str);

        String testData = String.format(
                """
                            {
                              "messages" : [ {
                                "role" : "system",
                                "content" : "- 일기 답변 및 문장 나누기 시스템입니다.\\n- 사용자가 자신의 일기를 입력하면, 일기에 대한 답변과 일기를 문장 단위로 나누어 이를 json 형식으로 응답합니다.\\n- 일기에 대한 답변은 사용자의 일기 내용을 파악하여, 적절한 공감이 담아 4문장 분량으로 표현합니다. \\n- 문장 나누기는 배열로 하여 각 문장이 배열에 들어가는 형태이며, 마침표로 구분될 때 문장이 너무 짧다면 앞이나 뒷 문장과 합쳐진 문장으로 분리합니다.\\n- 답변은 json 형식으로 출력합니다.\\n- 아래 예시를 참고합니다. 답변은 항상 아래 예시와 같은 포맷으로 나와야합니다.\\n\\n어시스턴트: \\n{\\n  \\"reply\\": \\"어제 늦게 잔 만큼 오늘 늦잠을 자서 하루가 조금 흐트러진 느낌이 들었겠어요. 하지만 중요한 역량검사를 준비하고 잘 마친 당신의 모습에서 집중력과 책임감이 느껴져요. 내일은 면접 준비로 정장을 대여하러 가는 설렘도 있으니, 좋은 일이 더 많이 이어질 것 같아요!\\",\\n  \\"sentences\\": [\\"어제 새벽에 유튜브를 보느라 늦게 자버렸다.\\", \\"그래서 오늘 오후 4시까지 늦잠을 잤다.\\", \\"오늘은 한진 역량검사를 봐야 해서 마음이 약간 조급했지만, 일단 밥을 먹고 역량검사를 준비했다.\\", \\"역량검사는 원래 보던 유형이 아니라 약간 당황했지만, 그래도 어느 정도 잘 본 것 같다.\\", \\"그리고 내일은 다음 주 면접 때 입고 갈 정장을 대여하러 가야 한다.\\", \\"우하하 설렌다.\\"]\\n}"
                              }, {
                                "role" : "user",
                                "content" : "어제 새벽에 유튜브를 보느라 늦게 자버렸다. 그래서 오늘 오후 4시까지 늦잠을 잤다. 오늘은 한진 역량검사를 봐야해서 마음이 약간 조급했지만, 일단 밥을 먹고 역량검사를 준비했다. 역량검사는 원래 보던 유형이 아니라 약간 당황했지만, 그래도 어느 정도 잘 본 것 같다. 그리고 내일은 다음 주 면접 때 입고 갈 정장을 대여하러 가야한다. 우하하 설렌다."
                              } ],
                              "topP" : 0.6,
                              "topK" : 0,
                              "maxTokens" : 1500,
                              "temperature" : 0.6,
                              "repeatPenalty" : 7.29,
                              "stopBefore" : [ ],
                              "includeAiFilters" : true,
                              "seed" : 0
                            }
                        """
        );
        System.out.println("testData = " + testData);

        HttpHeaders headers = headers();
        HttpEntity<Object> requestEntity = new HttpEntity<>(str, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println("response = " + response);
        System.out.println("response = " + response.getBody());

        return null;
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY","");
        headers.set("X-NCP-APIGW-API-KEY","");
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID","");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.TEXT_EVENT_STREAM));
        return headers;
    }
}
