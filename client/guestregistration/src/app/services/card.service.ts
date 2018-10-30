import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Card } from '../classes/card'

let httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  }),
  withCredentials: true
};

@Injectable()
export class CardService {

  constructor(private http: HttpClient) { }

  /**
   * sends request to server to get all cards
   */
  getCards() {
    return this.http.get('http://localhost:8080/api/cards', httpOptions);
  }

  /**
   * sends request to server to create new card
   * @param card card data
   */
  postCard(card: Card) {
    let data = {
      "cardNo": card.cardNo,
      "validFrom": card.validFrom,
      "validThru": card.validThru,
      "officeId": card.location,
      "cardType": card.cardType
    }
    return this.http.post('http://localhost:8080/api/cards', data, httpOptions);
  }

  /**
   * sends request to server to delete card with provided id
   * @param id id of the card that will be deleted
   */
  deleteCard(id: String) {
    return this.http.delete('http://localhost:8080/api/cards/' + id,  httpOptions);
  }

  /**
   * sends request to server to update card with provided id
   * @param card updated card data
   * @param id id of the car that will be updated
   */
  putCard(card: Card) {
    
    let data = {
      "id": card.id,
      "cardNo": card.cardNo,
      "validFrom": card.validFrom,
      "validThru": card.validThru,
      "officeId": card.location,
      "cardType": card.cardType,
      "cardAvailable": card.cardAvailable
    }
    return this.http.put('http://localhost:8080/api/cards', data, httpOptions);
  }

  /**
   * sends request to server to get a single card with provided id
   * @param id id of the card
   */
  getSingleCard(id: string){
    return this.http.get('http://localhost:8080/api/cards/'+id,  httpOptions);
  }

  /**
   * sends request to the server to with search phrase to receive all
   * cards that contain that phrase
   * @param text search phrase
   */
  findCards(text: string, type: string){
    return this.http.get('http://localhost:8080/api/cards/find?query='+text+'&type='+type,  httpOptions);
  }

  getTypes(){
    return this.http.get('http://localhost:8080/api/cards/types', httpOptions);
  }

  getAvailableCards(){
    return this.http.get('http://localhost:8080/api/cards/available',  httpOptions);
  }
}
