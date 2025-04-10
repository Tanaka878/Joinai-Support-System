package com.joinai_support;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    private final VectorStore vectorStore;
    private final JdbcClient jdbcClient;
    @Value("classpath:/support.pdf")
    private Resource resource;

    public DataLoader(VectorStore vectorStore, JdbcClient jdbcClient) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
    }

    @PostConstruct
    public void init() {
        Integer count = jdbcClient.sql("SELECT COUNT(*) from vector_store")
                .query(Integer.class).single();

        System.out.println("count the number of records : " + count);

        if (count ==0) {
            System.out.println("No vector store found");
            PdfDocumentReaderConfig config =
                    PdfDocumentReaderConfig.builder().withPagesPerDocument(1).build();

            PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);

            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(reader.get());
            System.out.println("Read complete.");
        }
    }

}
