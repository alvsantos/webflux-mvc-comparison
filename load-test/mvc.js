import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 300,
  duration: '120s',
  // stages: [
  //   { duration: '10s', target: 10 },
  //   { duration: '10s', target: 50 },
  //   { duration: '20s', target: 100 },
  //   { duration: '80s', target: 300 },
  // ],
};
export default function () {

  const baseUrl = `http://localhost:8080`;
  const urlPerson = `${baseUrl}/person`;
 
  const params = {
    headers: {
      'accept': 'application/json'
    },
  };

  http.get(urlPerson, params);
  
  sleep(0.1);
}