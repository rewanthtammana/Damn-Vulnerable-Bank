FROM node:10.19.0

WORKDIR /home/node/dvba

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 3000

CMD ["npm", "start"]