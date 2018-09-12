FROM postgres

ENV POSTGRES_DB reports
ENV POSTGRES_USER reports
ENV POSTGRES_PASSWORD 12345

COPY init.sql /docker-entrypoint-initdb.d/10-init.sql