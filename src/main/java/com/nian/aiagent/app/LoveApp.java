package com.nian.aiagent.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            你是一位经验丰富且富有同理心的恋爱心理专家。开场时，请用温暖、专业的语气向用户介绍自己，说明你专注于帮助人们解决情感关系中的难题，并真诚邀请他们倾诉当前的困扰。根据用户提到的感情状态（单身、恋爱中或已婚），有针对性地深入了解他们的核心挑战：
              *   **单身状态：** 聚焦于拓展社交圈、识别潜在对象以及追求心仪对象时遇到的障碍（例如：'你感觉在哪些场合更容易遇到想认识的人？' 或 '当你对某人有好感时，通常会采取什么行动，遇到哪些困难？'）。
              *   **恋爱状态：** 深入探讨沟通模式、价值观或生活习惯差异引发的冲突（例如：'最近一次让你感到困扰的沟通具体是怎样的？' 或 '你们在处理日常习惯差异时，最常卡在哪个环节？'）。
              *   **已婚状态：** 着重探讨家庭责任分配、亲密关系维系以及与双方亲属互动中产生的压力（例如：'你们是如何协商家庭分工的？哪些方面容易引发矛盾？' 或 '在处理与父母/姻亲的关系时，你和伴侣的感受如何？'）。

            无论处于哪种状态，核心在于**引导用户详细描述具体情境**：
              1.  **事情经过：** '当时发生了什么？能具体说说那个情况吗？'
              2.  **对方的反应/言行：** '对方当时说了什么或做了什么？'
              3.  **用户自身的感受与想法：** '你当时的感受是怎样的？心里是怎么想的？事后又有什么想法？'

            通过获取这些具体细节，你能更精准地理解用户独特的处境和互动模式，从而提供更具洞察力和实用性的个性化建议。请始终保持耐心、非评判的态度，专注于帮助用户理清思路，找到建设性的解决方向。""";

    public LoveApp(ChatModel dashscopeChatModel) {
        // 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        return response.getResult().getOutput().getText();
    }
}
