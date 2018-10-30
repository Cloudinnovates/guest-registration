import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Event } from '../../classes/event';
import { Card } from '../../classes/card';
import { EventService } from '../../services/event.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ParticipantService } from '../../services/participant.service';
import { MatPaginator, MatTableDataSource, MatSort } from '@angular/material';
import { FormControl } from '@angular/forms';
import { CardService } from '../../services/card.service';
import { EventParticipant } from '../../classes/event_participant';
import { AgentService } from '../../services/agent.service';
import { CardSelectDialog } from '../../shared/card-dialog/card-select-dialog'

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})

export class EventDetailsComponent implements OnInit {

  event: Event = new Event();
  participants: EventParticipant[] = [];
  availableCards: Card[] = [];

  dataSource: MatTableDataSource<EventParticipant>;
  displayedColumns: string[] = ['participant', 'phone', 'address', 'gender', 'date_of_birth', 'work_place', 'cardNo', 'actions']
  myControl = new FormControl();
  options: any[] = [];
  showDetailsFuncionality = true;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _router: Router,
    private _activeRoute: ActivatedRoute,
    private _eventService: EventService,
    private _participantService: ParticipantService,
    private _agentService: AgentService,
    private _cardService: CardService,
    private dialog: MatDialog) { }

  ngOnInit() {
    console.log()
    if(this._router.url.split("/")[1] == "event-checkin"){
      this.showDetailsFuncionality = false;
    }else {
      this.showDetailsFuncionality = true;
    }
    this._activeRoute.params.subscribe(param => {
      this.getEventInfo(param['id']);
      this.getEventParticipants(param['id']);
      this.getAgentsNotInEvent(param['id'])

    })
  }

  getEventInfo(eventId: string) {
    this._eventService.getEvent(eventId).subscribe(
      data => {
        this.event.id = data['id'];
        this.event.dateFrom = data['dateFrom'];
        this.event.dateTo = data['dateTo'];
        this.event.eventName = data['eventName'];
        this.event.eventType = data['eventType'];
        this.event.location = data['location'];
        this.event.manager = data['manager'];
        this.event.participantNo = data['participantNo']
        this.event.private = data['private']
      },
      err => {
        console.log(err);
      })
  }

  getEventParticipants(eventId: string) {
    this.participants = [];
    this._participantService.getAllParticipants(eventId).subscribe(
      data => {
        if (Object.keys(data).length > 0) {
          Object.keys(data).forEach(key => {
            let participant = new EventParticipant();
            participant.eventId = this.event.id;
            participant.id = data[key]['id']
            this._agentService.getAgent(data[key]['agentId']).subscribe(
              resAgent => {
                let agent = {
                  id: resAgent['id'],
                  name: resAgent['name'],
                  surname: resAgent['surname'],
                  phone: resAgent['phone'],
                  address: resAgent['address'],
                  gender: resAgent['gender'],
                  date_of_birth: resAgent['date_of_birth'],
                  work_place: resAgent['work_place']
                };

                if (!agent.address) {
                  agent.address = "Not provided";
                }

                if (!agent.gender) {
                  agent.gender = "Not provided";
                }

                if (!agent.date_of_birth) {
                  agent.date_of_birth = "Not provided";
                } else {
                  agent.date_of_birth = agent.date_of_birth.substring(0, 10);
                }

                if (!agent.work_place) {
                  agent.work_place = "Not provided";
                }
                participant.agent = agent;
                if (data[key]['cardId']) {
                  this._cardService.getSingleCard(data[key]['cardId']).subscribe(
                    resCard => {
                      let card: Card = {
                        id: resCard['id'],
                        cardNo: resCard['cardNo'],
                        location: resCard['officeId'],
                        cardType: resCard['cardType'],
                        validThru: resCard['validThru'],
                        validFrom: resCard['validFrom'],
                        cardAvailable: resCard['cardAvailable']
                      };
                      participant.card = card;
                      this.participants.push(participant);
                      this.dataSource = new MatTableDataSource(this.participants);
                      this.dataSource.paginator = this.paginator;
                      this.dataSource.sort = this.sort;
                    }
                  )
                } else {
                  this.participants.push(participant);
                  this.dataSource = new MatTableDataSource(this.participants);
                  this.dataSource.paginator = this.paginator;
                  this.dataSource.sort = this.sort;
                }
              }
            )

          });
        } else {
          this.participants = []
          this.dataSource = new MatTableDataSource(this.participants);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        }
      },
      err => {
        console.log(err);
      }
    )
  }

  addNewParticipant() {
    let value = (<HTMLInputElement>document.getElementById('search-input')).value;
    let agent = this.options.find(agent => agent.name == value)
    if (agent) {
      this._participantService.addParticipantToEvent(this.event.id, agent.id).subscribe(
        data => {
          this.getEventParticipants(this.event.id);
          this.getAgentsNotInEvent(this.event.id);

        },
        err => {
          console.log(err);
        }
      )
    }
  }

  deleteButtonPressed(participant: EventParticipant) {
    this._participantService.deleteParticipantfromEvent(participant.id).subscribe(
      data => {
        if (participant.card) {
          this.makeCardAvailable(participant.card);
        }
        this.getEventParticipants(this.event.id);
        this.getAgentsNotInEvent(this.event.id);
      },
      err => {
        console.log(err);
      }
    )
  }

  searchAgent(event) {
    this._participantService.findAgentsNotInEvent(this.event.id, event).subscribe(
      data => {
        this.updateOptions(data);
      },
      err => {
        console.log(err);
      }
    )
  }

  updateOptions(data) {
    this.options = [];
    Object.keys(data).forEach(
      key => {
        let agent = {
          id: data[key]['id'],
          name: data[key]['name'] + " " + data[key]['surname'] 
        };
        this.options.push(agent);
      })
  } 

  getAgentsNotInEvent(eventId) {
    this._participantService.getAgentsNotInEvent(eventId).subscribe(
      data => {
        this.updateOptions(data);
      },
      err => {
        console.log(err)
      }
    )
  }

  createNewAgent() {
    this._router.navigate(['/agents/add']);
  }


  openDialog(eventParticipant: EventParticipant): void {
    const dialogRef = this.dialog.open(CardSelectDialog, {
      width: '250px',
      data: eventParticipant
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.assignCardToParticipant(result)
      }else {
        this.getEventParticipants(this.event.id);
      }
    });
  }

  makeCardAvailable(card: Card) {
    card.cardAvailable = true;
    this._cardService.putCard(card).subscribe(
      data => {
        this.getEventParticipants(this.event.id);
      },
      err => {
        console.log(err)
      }
    )
  }

  assignCardToParticipant(data: EventParticipant) {
    let participant = {
      "id": data.id,
      "eventId": data.eventId,
      "agentId": data.agent.id,
      "cardId": data.card.id
    }

    this._participantService.updateParticipant(participant).subscribe(
      resData => {
        this.makeCardUnavailable(data)
      },
      err => {
        console.log(err)
      }
    )
  }

  makeCardUnavailable(data: EventParticipant) {
    data.card.cardAvailable = false;
    this._cardService.putCard(data.card).subscribe(
      data => {
        this.getEventParticipants(this.event.id);
      },
      err => {
        console.log(err);
      }
    )
  }

  removeCard(data: EventParticipant){
    let participant = {
      "id": data.id,
      "eventId": data.eventId,
      "agentId": data.agent.id,
      "cardId": ""
    }

    this._participantService.updateParticipant(participant).subscribe(
      resData => {
        this.makeCardAvailable(data.card)
        
      },
      err => {
        console.log(err)
      }
    )
  }
}

