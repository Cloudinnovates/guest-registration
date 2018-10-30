import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AgentService } from '../../services/agent.service';
import { DatePipe } from '@angular/common';
import { MatDialog } from '@angular/material';
import { DuplicateErrorComponent } from '../../shared/error-window/duplicate-error.component';
import { Employee } from '../../classes/employee';
import { EmployeeService } from '../../services/employee.service';
import { CardService } from '../../services/card.service';
import { Card } from '../../classes/card';
import { Agent } from '../../classes/agent';
import { OfficeService}  from '../../services/office.service';
import { Office } from '../../classes/offices';

@Component ({
  selector: 'update-employee',
  templateUrl: './update-employee.component.html',
  styleUrls: ['./edit-employee.component.css']
})

export class UpdateEmployeeComponent implements OnInit {
  employee: Employee = new Employee;
  editing_employee_id: string;

  firstName: string;
  firstName_check: string; // to check for duplicate, if the first name have been modified/changed
  lastName: string;
  lastName_check: string; // to check for duplicate, if the last name have been modified/changed
  card_no: string;
  cardNo_check: string; // to check, if another card was selected; stores initial card number
  phone: string;
  position: string;
  phone_check: string; // to check, if agent phone number have been changed; stores initial phone number
  needToCheckAgent: boolean;

  employment_date: string;
  card_issue_date: string;
  dismissal_date: string;
  initial_cardId: string;
  initial_office: string;
  initial_office_id: string;
  selected_office_id: string;
  cards: Card[];
  offices: Office[];
  card: Card;
  agent: Agent = new Agent();

  constructor(private employeeService: EmployeeService, private router: Router, private officeService: OfficeService,
              private agentService: AgentService, private cardService: CardService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.employeeService.currentMessage.subscribe(editing_employee_id => this.editing_employee_id = editing_employee_id);
    this.getEmployee(this.editing_employee_id);
    this.getOffices();
    this.employeeService.getEmployee(this.editing_employee_id).subscribe(data => {
      this.getCards(data['officeId']);
      this.initial_office_id = data['officeId'];
      this.selected_office_id = data['officeId'];
    });
  }

  getEmployee(id: string): void {
    this.employeeService.getEmployee(id)
      .subscribe(data => {
          this.employee = Object(data);
          this.employment_date = this.employee.employment_date.toString().substring(0, 10);
          this.card_issue_date = this.employee.card_issue_date.toString().substring(0, 10);
          this.initial_cardId = this.employee.cardId;
          this.position = this.employee.position;

          this.officeService.getOffice(this.employee.officeId).subscribe(office => {
            this.initial_office = office['name'];
          });

          this.agentService.getAgent(this.employee.agentId).subscribe(agent => {

            this.firstName = agent['name'];
            this.firstName_check = agent['name'];
            this.lastName = agent['surname'];
            this.lastName_check = agent['surname'];
            this.phone = agent['phone'];
            this.phone_check = agent['phone'];
          });

          this.cardService.getSingleCard(this.employee.cardId).subscribe(card => {
            this.card_no = card['cardNo'];
            this.cardNo_check = card['cardNo'];
          });
        },
        err => {
          if (err['status'] == 401) {
            this.router.navigate(['/auth-error']);
          } else {
            console.error(err);
          }
        });
  }

  getOffices() {
    this.officeService.getOffices().subscribe(offices => {
      this.offices = [];
        Object.keys(offices).forEach(key => {
          this.offices.push(offices[key]);
        });
      },
      err => {
        if (err['status'] == 401) {
          this.router.navigate(['/auth-error']);
        } else {
          console.error(err);
        }
      });
  }


  getCards(officeId: string) {
    this.card_no = this.cardNo_check;
    this.cardService.getCards().subscribe(cards => {
        this.cards = [];
        Object.keys(cards).forEach(key => {
          if ((cards[key]['officeId'] == officeId) && (cards[key]['cardAvailable'] == true)
            && (cards[key]['cardType']) == 'Full-time Employee') {
            this.cards.push(cards[key]);
          }
        });
      },
      err => {
        if (err['status'] == 401) {
          this.router.navigate(['/auth-error']);
        }
      });
  }

  editEmployee() {
    if ((this.firstName.length > 0) && (this.lastName.length > 0) && (this.phone.length > 0) && (this.employee.position.length > 0)) {

      this.needToCheckAgent = (this.firstName != this.firstName_check) || (this.lastName != this.lastName_check);

      this.agentService.checkAgent(this.firstName, this.lastName).subscribe(agent => {
        if ((agent == true) && this.needToCheckAgent) {
          this.dialog.open(DuplicateErrorComponent, {width: '360px'});
          return;
        } else {

          if (this.dismissal_date != null) {
            this.cardService.getSingleCard(this.initial_cardId).subscribe(card => {
              this.card = {
                id: card['id'],
                cardNo: card['cardNo'],
                location: card['officeId'],
                cardType: card['cardType'],
                validThru: card['validThru'],
                validFrom: card['validFrom'],
                cardAvailable: true
              };
              this.cardService.putCard(this.card).subscribe();
            });
            this.employee.dismissal_date = new Date(new DatePipe('en-US').transform(this.dismissal_date, 'MM-dd-yyyy') + ' 12:00:00').getTime();
          } else {

            this.employee.employment_date = new Date(new DatePipe('en-US').transform(this.employment_date, 'MM-dd-yyyy') + ' 12:00:00').getTime();
            this.employee.card_issue_date = new Date(new DatePipe('en-US').transform(this.card_issue_date, 'MM-dd-yyyy') + ' 12:00:00').getTime();
            this.employee.position = this.position;

            if (this.needToCheckAgent) {
              this.agentService.getAgent(this.employee.agentId).subscribe(agentData => {
                this.agent = Object(agentData);
                this.agent.name = this.firstName;
                this.agent.surname = this.lastName;
                if (this.phone_check != this.phone) {
                  this.agent.phone = this.phone;
                }
                this.agentService.updateAgent(this.agent).subscribe();
              });

            } else {

              if (this.phone_check != this.phone) {
                this.agentService.getAgent(this.employee.agentId).subscribe(agentData => {
                  this.agent = Object(agentData);
                  this.agent.phone = this.phone;
                  this.agentService.updateAgent(this.agent).subscribe();
                });
              }
            }

            if (this.cardNo_check != this.card_no) {
              this.cardService.getSingleCard(this.initial_cardId).subscribe(card => {
                this.card = {
                  id: card['id'],
                  cardNo: card['cardNo'],
                  location: card['officeId'],
                  cardType: card['cardType'],
                  validThru: card['validThru'],
                  validFrom: card['validFrom'],
                  cardAvailable: true
                };
                this.cardService.putCard(this.card).subscribe();
              });

              this.cardService.getSingleCard(this.card_no).subscribe(card => {
                this.card = {
                  id: card['id'],
                  cardNo: card['cardNo'],
                  location: card['officeId'],
                  cardType: card['cardType'],
                  validThru: card['validThru'],
                  validFrom: card['validFrom'],
                  cardAvailable: false
                };
                this.cardService.putCard(this.card).subscribe();
              });
            }
          }

          if (this.cardNo_check != this.card_no) {
            this.initial_cardId = this.card_no;
          }

          this.cardService.getSingleCard(this.initial_cardId).subscribe(card => {

            this.employee.officeId = card['officeId'];

            this.employee.cardId = this.initial_cardId;

            this.employeeService.updateEmployee(this.employee).subscribe(data => {
                this.router.navigate(['/employees']);
              }, err => {
                if (err['status'] == 401) {
                  this.router.navigate(['/auth-error']);
                } else {
                  console.error(err);
                }
              },
              () => console.log('Done'));
          });
        }
      });
    }
  }

  initialOfficeIdChanger(selected_office_id: string) {
    this.selected_office_id = selected_office_id;
  }

  cancelBtnPressed() {
    this.router.navigate(['/employees']);
  }

}
