import { Component, Inject } from "@angular/core";
import { Card } from "../../classes/card";
import { MAT_DIALOG_DATA } from "@angular/material";
import { EventParticipant } from "../../classes/event_participant";
import { CardService } from "../../services/card.service";


@Component({
    selector: 'card-select-dialog',
    templateUrl: './card-select-dialog.html',
  })
  export class CardSelectDialog {
    cards: Card[] = [];
    constructor(@Inject(MAT_DIALOG_DATA) public data: EventParticipant,
      private _cardService: CardService) {
  
      this._cardService.getAvailableCards().subscribe(
        data => {
          Object.keys(data).forEach(
            key => {
              let card = {
                id: data[key]['id'],
                cardNo: data[key]['cardNo'],
                location: data[key]['officeId'],
                cardType: data[key]['cardType'],
                validThru: data[key]['validThru'],
                validFrom: data[key]['validFrom'],
                cardAvailable: data[key]['cardAvailable']
              };
              this.cards.push(card);
            }
          )
        },
        err => {
          console.log(err)
        }
      )
    }
  }