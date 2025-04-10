-- Enable required PostgreSQL extensions
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the vector_store table
CREATE TABLE IF NOT EXISTS vector_store (
                                            id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata jsonb,
    embedding vector(1536)
    );

-- Create an HNSW index for vector similarity search
CREATE INDEX IF NOT EXISTS vector_store_embedding_idx ON vector_store
    USING HNSW (embedding vector_cosine_ops);