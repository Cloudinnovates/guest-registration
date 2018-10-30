import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { DialogComponent } from '../../shared/dialog-window/dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { Employee } from '../../classes/employee';
import { EmployeeService } from '../../services/employee.service';
import { CardService } from '../../services/card.service';
import { OfficeService } from '../../services/office.service';
import { Card } from '../../classes/card';
import { AgentService } from '../../services/agent.service';


@Component({
  selector: 'all-employees',
  templateUrl: './all-employees.component.html',
  styleUrls: ['./all-employees.component.css']
})

export class AllEmployeesComponent implements OnInit {
  employees: Employee[];
  searchField: string;
  dataSource: MatTableDataSource<Employee>;
  card: Card;

  displayedColumns: string[] = ['employee', 'office', 'position', 'phone', 'employment_date', 'card_no', 'card_issue_date', 'actions'];


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private employeeService: EmployeeService, public dialog: MatDialog,
              private router: Router, private activeRoute: ActivatedRoute, private agentService: AgentService,
              private cardService: CardService, private officeService: OfficeService) {}

  ngOnInit(): void {
    this.getEmployees();
  }

  getEmployees() {
    this.employeeService.getAllEmployees().subscribe(data => {
      this.employees = [];
      if (data != null) {
        Object.keys(data).forEach(key => {
          this.cardService.getSingleCard(data[key]['cardId']).subscribe(card => {

            this.agentService.getAgent(data[key]['agentId']).subscribe(agent => {

              this.officeService.getOffice(data[key]['officeId']).subscribe(office => {

                  let employee = {
                    id: data[key]['id'],
                    agentId: agent['name'] + ' ' + agent['surname'],
                    position: data[key]['position'],
                    phone: agent['phone'],
                    employment_date: data[key]['employment_date'].substring(0, 10),
                    card_issue_date: data[key]['card_issue_date'].substring(0, 10),
                    dismissal_date: data[key]['dismissal_date'],
                    cardId: card['cardNo'],
                    officeId: office['name']
                  };

                  if (employee.dismissal_date == null) {
                    this.employees.push(employee);
                    this.dataSource = new MatTableDataSource(this.employees);
                    this.dataSource.paginator = this.paginator;
                    this.dataSource.sort = this.sort;
                  }
                },
                err => {
                  if (err['status'] == 401) {
                    this.router.navigate(['/auth-error']);
                  } else {
                    console.error(err);
                  }
                });
            });
          });
        });
      }
    });
  }


  deleteEmployee(employee: Employee, index: number): void {
    let dialogRef = this.dialog.open(DialogComponent, {width: '270px'});

    dialogRef.afterClosed().subscribe(data => {
        if (data == true) {
          this.employees = this.employees.filter(h => h !== employee);

          this.employeeService.getEmployee(employee.id).subscribe(emp => {
            this.cardService.getSingleCard(emp['cardId']).subscribe(card => {
              this.card = {
                id: card['id'],
                cardNo: card['cardNo'],
                location: card['officeId'],
                cardType: card['cardType'],
                validThru: card['validThru'],
                validFrom: card['validFrom'],
                cardAvailable: true
              };

              this.cardService.putCard(this.card).subscribe(response => {
                this.employeeService.deleteEmployee(employee.id).subscribe();
              });
            });
          });


          this.dataSource = new MatTableDataSource(this.employees);
          this.dataSource.paginator = this.paginator;
          if ((index == 0) && (this.paginator.pageIndex != 0)) {
            this.dataSource.paginator.previousPage();
          }
          this.dataSource.sort = this.sort;
        }
      },
      err => {
        if (err['status'] == 401) {
          this.router.navigate(['/auth-error']);
        } else {
          console.error(err);
        }
      }
    );

  }

  editingEmployeeId(id : string) {
    this.employeeService.changeMessage(id);
  }

  searchInput() {
    this.dataSource = null;
    if (this.searchField.length > 0) {

      this.employeeService.findEmployeeInFullList(this.searchField).subscribe(data => {

        this.employees = [];
        if (data != null) {

          Object.keys(data).forEach(key => {
            this.cardService.getSingleCard(data[key]['cardId']).subscribe(card => {

              this.agentService.getAgent(data[key]['agentId']).subscribe(agent => {

                this.officeService.getOffice(data[key]['officeId']).subscribe(office => {

                    let employee = {
                      id: data[key]['id'],
                      agentId: agent['name'] + ' ' + agent['surname'],
                      position: data[key]['position'],
                      phone: agent['phone'],
                      employment_date: data[key]['employment_date'].substring(0, 10),
                      card_issue_date: data[key]['employment_date'].substring(0, 10),
                      dismissal_date: data[key]['dismissal_date'],
                      cardId: card['cardNo'],
                      officeId: office['name']
                    };

                    if (employee.dismissal_date == null) {
                      this.employees.push(employee);
                      this.dataSource = new MatTableDataSource(this.employees);
                      this.dataSource.paginator = this.paginator;
                      this.dataSource.sort = this.sort;
                    }
                  },
                  err => {
                    if (err['status'] == 401) {
                      this.router.navigate(['/auth-error']);
                    } else {
                      console.error(err);
                    }
                  });
              });
            });
          });
        }
      });
    } else {
      this.getEmployees();
    }
  }

}
