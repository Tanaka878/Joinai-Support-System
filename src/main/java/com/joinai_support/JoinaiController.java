package com.joinai_support;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/joinai")
public class JoinaiController {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    private static final String PROMPT = """
            Your task is to answer the questions about the Joinai Fintech. Use the information from the DOCUMENTS
            section to provide accurate answers. If unsure or if the answer isn't found in the DOCUMENTS section, 
            simply state that you don't know the answer.

            QUESTION:
            {input}

            DOCUMENTS:
            {documents}
            """;

    public JoinaiController(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/chat")
    public String simplify(@RequestParam(value = "question", defaultValue = "what is joinai") String question) {
        try {
            String documents = findSimilarData(question);
            PromptTemplate template = new PromptTemplate(PROMPT);
            Map<String, Object> promptParameters = new HashMap<>();
            promptParameters.put("input", question);
            promptParameters.put("documents", documents);

            return chatModel
                    .call(template.create(promptParameters))
                    .getResult()
                    .getOutput()
                    .getText();
        } catch (Exception e) {
            // Add logging here
            return "An error occurred while processing your request. Please try again.";
        }
    }

    private String findSimilarData(String question) {
        try {
            // Use a builder or appropriate constructor for SearchRequest
            SearchRequest searchRequest = SearchRequest.builder()
                    .query(question)
                    .topK(5)
                    .build();

            List<Document> documents = vectorStore.similaritySearch(searchRequest);

            return documents.stream()
                    .map(Document::getText) // Ensure this method exists
                    .collect(Collectors.joining("\n")); // Use a separator for readability
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
            return "No relevant documents found.";
        }
    }


}
