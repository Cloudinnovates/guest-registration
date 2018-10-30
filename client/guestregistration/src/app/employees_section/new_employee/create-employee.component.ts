import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { DuplicateErrorComponent } from '../../shared/error-window/duplicate-error.component';
import { MatDialog } from '@angular/material';
import { Employee } from '../../classes/employee';
import { EmployeeService } from '../../services/employee.service';
import { CardService } from '../../services/card.service';
import { Card } from '../../classes/card';
import { AgentService } from '../../services/agent.service';
import { Agent } from '../../classes/agent';
import { OfficeService } from '../../services/office.service';
import { Office } from '../../classes/offices';


@Component ({
  selector: 'create-employee',
  templateUrl: './create-employee.component.html',
  styleUrls: ['./create-employee.component.css']
})

export class CreateEmployeeComponent implements OnInit {
  employee: Employee = new Employee();
  office_id: string;
  cards: Card[];
  card: Card;
  agents: Agent[];
  office: Office;

  constructor(private employeeService: EmployeeService, private cardService: CardService, private officeService: OfficeService,
              private agentService: AgentService, private router: Router, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.employeeService.currentMessage.subscribe(office_id => this.office_id = office_id);
    this.officeService.getOffice(this.office_id).subscribe(office => {
      this.office = office['name'];
    });
    this.getCards();
    this.getAgents();
  }

  createEmployee(agentId, position, employment_date, card_issue_date, card_no) {

    if ((agentId != null) && (position.length > 0) && (employment_date.length > 0) && (card_issue_date.length > 0) && (card_no != null)) {
      this.employeeService.checkEmployee(agentId).subscribe(data => {

        if (data == true) {
          this.dialog.open(DuplicateErrorComponent, {width: '360px'});
          return;

        } else {
          this.employee.agentId = agentId;
          this.employee.position = position;

          let date = new Date(new DatePipe('en-US').transform(employment_date, 'MM-dd-yyyy') + ' 12:00:00');
          this.employee.employment_date = date.getTime();

          date = new Date(new DatePipe('en-US').transform(card_issue_date, 'MM-dd-yyyy') + ' 12:00:00');
          this.employee.card_issue_date = date.getTime();

          this.employee.cardId = card_no;
          this.employee.officeId = this.office_id;

          this.cardService.getSingleCard(card_no).subscribe(data => {
            this.card = {
              id: data['id'],
              cardNo: data['cardNo'],
              location: data['officeId'],
              cardType: data['cardType'],
              validThru: data['validThru'],
              validFrom: data['validFrom'],
              cardAvailable: false
            };

            this.cardService.putCard(this.card).subscribe();
          });


          this.employeeService.createEmployee(this.employee)
            .subscribe((data) => {
                this.router.navigate(['/office/' + this.office_id]);
              }, err => {
                if (err['status'] == 401) {
                  this.router.navigate(['/auth-error']);
                } else {
                  console.error(err);
                }
              },
              () => console.log('Done'));

        }
      });
  }
  }

  getCards() {
    this.cardService.getCards().subscribe(cards => {
        this.cards = [];
        Object.keys(cards).forEach(key => {
          if ((cards[key]['officeId'] == this.office_id) && (cards[key]['cardAvailable'] == true)
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

  getAgents() {
    this.agentService.getAgents().subscribe(agents => {
      this.agents = [];
      Object.keys(agents).forEach(key => {
        this.agents.push(agents[key]);
      });
    });
  }

  cancelBtnPressed() {
    this.router.navigate(['/office/' + this.office_id]);
  }

}
