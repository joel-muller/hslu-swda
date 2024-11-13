FROM mysql:latest
COPY ./central_warehouse.sql .
RUN mysql -u root -p central_warehouse < central_warehouse.sql

