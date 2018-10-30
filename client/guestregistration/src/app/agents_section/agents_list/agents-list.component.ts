import { Component, OnInit, ViewChild } from '@angular/core';


import {MatDialog, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {DialogComponent} from '../../shared/dialog-window/dialog.component';
import {Router} from '@angular/router';
import {Agent} from '../../classes/agent';
import {AgentService} from '../../services/agent.service';

@Component({
  selector: 'agents-list',
  templateUrl: './agents-list.component.html',
  styleUrls: ['./agents-list.component.css']
})

export class AgentsListComponent implements OnInit {
  agents: Agent[];
  editing_agent_id: string;
  searchField: string;
  dataSource: MatTableDataSource<Agent>;

  displayedColumns: string[] = ['participant', 'phone', 'address', 'gender', 'date_of_birth', 'work_place', 'actions'];


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private agentService: AgentService, public dialog: MatDialog, private router: Router) {}

  ngOnInit(): void {
    this.getAgents();
    this.agentService.currentMessage.subscribe(editing_agent_id => this.editing_agent_id = editing_agent_id);
  }

  getAgents(): void {
    this.agentService.getAgents()
      .subscribe(agents => {
          this.agents = [];
          Object.keys(agents).forEach(key => {
            let agent = {
              id: agents[key]['id'],
              name: agents[key]['name'],
              surname: agents[key]['surname'],
              phone: agents[key]['phone'],
              address: agents[key]['address'],
              gender: agents[key]['gender'],
              date_of_birth: agents[key]['date_of_birth'],
              work_place: agents[key]['work_place']
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
              agent.date_of_birth = agent.date_of_birth.substring(0,10);
            }

            if (!agent.work_place) {
              agent.work_place = "Not provided";
            }

            this.agents.push(agent);

          });
          this.dataSource = new MatTableDataSource(this.agents);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        err => {
          if (err['status'] == 401){
            this.router.navigate(['/auth-error']);
          }else{
            console.error(err);
          }
        }
      );
  }


  deleteAgent(agent : Agent, index: number): void {
    let dialogRef = this.dialog.open(DialogComponent, {width:'270px'});

    dialogRef.afterClosed().subscribe(data => {
        if (data == true) {
          this.agents = this.agents.filter(h => h !== agent);
          this.agentService.deleteAgent(agent.id).subscribe();
          this.dataSource = new MatTableDataSource(this.agents);
          this.dataSource.paginator = this.paginator;
          if ((index == 0) && (this.paginator.pageIndex != 0)) {
            this.dataSource.paginator.previousPage();
          }
          this.dataSource.sort = this.sort;
        }
      },
      err => {
        if (err['status'] == 401){
          this.router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      }
    )

  }

  editingAgentId(id : string) {
    this.agentService.changeMessage(id);
  }

  searchInput() {
    this.dataSource = null;
    if (this.searchField.length > 0) {
      this.agentService.findAgents(this.searchField)
        .subscribe(data => {
            if(data) {
              this.agents = [];
              Object.keys(data).forEach(key => {
                let agent = {
                  id: data[key]['id'],
                  name: data[key]['name'],
                  surname: data[key]['surname'],
                  phone: data[key]['phone'],
                  address: data[key]['address'],
                  gender: data[key]['gender'],
                  date_of_birth: data[key]['date_of_birth'],
                  work_place: data[key]['work_place']
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
                  agent.date_of_birth = agent.date_of_birth.substring(0,10);
                }

                if (!agent.work_place) {
                  agent.work_place = "Not provided";
                }

                this.agents.push(agent);

              });
                this.dataSource = new MatTableDataSource(this.agents);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
              }},
          err => {
            if (err['status'] == 401){
              this.router.navigate(['/auth-error']);
            }else{
              console.error(err);
            }
          }
        );
    }else {
      this.getAgents();
    }
  }

}
