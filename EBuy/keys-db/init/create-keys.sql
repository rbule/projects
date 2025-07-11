CREATE TABLE IF NOT EXISTS api_key (
      user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
      secret VARCHAR(48) NOT NULL
);

INSERT INTO api_key (secret) VALUES
     ('9e26e2c2-4c3a-4a9b-b2b1-0b3f3f2faed1'),
     ('e1f6c7a0-2333-4b77-90dc-20e4e3d1a8b0'),
     ('b9a4f32f-1db2-4420-a0b3-c3a0d9a93f0a'),
     ('b5c9d2a7-6ea9-4c5b-90d5-cc8e0cf9c4b2'),
     ('a8f3e8f1-439d-4bd3-9f5d-71561a8f1aa7'),
     ('01dd37c2-5c42-4a84-b0f6-60a1b32478ee'),
     ('d733c01d-feb5-4c2d-9967-23b88cd709af'),
     ('f27ea65f-8fc6-4f3e-b7f1-8c1e0a934cff'),
     ('6a8fbf4e-bf21-46c6-9d34-56e703f1a7ad'),
     ('9d831aa5-8e4e-47c4-988e-6abf7a11a023');
