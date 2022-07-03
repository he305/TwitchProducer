import axios from 'axios';

const PERSON_API_BASE_URL = '/api/v1/person';

class PersonService {
    getPersons() {
        return axios.get(PERSON_API_BASE_URL);
    }

    createPerson(person) {
        return axios.post(PERSON_API_BASE_URL, person);
    }
}

export default new PersonService();