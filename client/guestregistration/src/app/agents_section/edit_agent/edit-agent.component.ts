import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Agent} from '../../classes/agent';
import {AgentService} from '../../services/agent.service';
import {DatePipe} from '@angular/common';
import {MatDialog} from '@angular/material';
import {DuplicateErrorComponent} from '../../shared/error-window/duplicate-error.component';

@Component ({
  selector: 'edit-agent',
  templateUrl: './edit-agent.component.html',
  styleUrls: ['./edit-agent.component.css']
})

export class EditAgentComponent implements OnInit {
  agent: Agent = new Agent;
  anotherAgent: Agent;
  editing_agent_id: string;
  agents: Agent[];
  editAgentForm: FormGroup;
  submitted = false;
  date_of_birth = "";
  needToCheckAgent: boolean;
  firstName: string;
  lastName: string;

  constructor(private agentService: AgentService, private formBuilder: FormBuilder, private router: Router, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.agentService.currentMessage.subscribe(editing_agent_id => this.editing_agent_id = editing_agent_id);
    this.getAgent(this.editing_agent_id);
    this.createForm();
  }

  getAgent(id: string): void {
    this.agentService.getAgent(id)
      .subscribe(data => {
          this.anotherAgent = Object(data);
          if (this.anotherAgent.date_of_birth) {
            this.date_of_birth = data['date_of_birth'].substring(0, 10);
          }
          this.agent = this.anotherAgent;
          this.firstName = this.agent.name;
          this.lastName = this.agent.surname;
        },
        err => {
          if (err['status'] == 401) {
            this.router.navigate(['/auth-error']);
          } else {
            console.error(err);
          }
        },
        () => console.log('Completed')
      );
  }

  editAgent(agentData: Agent): void {
    this.needToCheckAgent = false;

    this.submitted = true;
    // stop here if form is invalid
    if (this.editAgentForm.invalid) {
      return;
    }

    if((this.firstName != agentData.name) || (this.lastName != agentData.surname)) {
      this.needToCheckAgent = true;
    }

    this.agentService.checkAgent(agentData.name, agentData.surname).subscribe(data => {
      if (data == true && this.needToCheckAgent) {
        this.dialog.open(DuplicateErrorComponent, {width: '360px'});
        return;
      } else {
        agentData.date_of_birth = new Date(new DatePipe('en-US').transform(this.date_of_birth, 'MM-dd-yyyy') + ' 12:00:00').getTime();
        this.agentService.updateAgent(agentData)
          .subscribe((data) => {
              this.router.navigate(['/agents']);
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

  createForm() {
    this.editAgentForm = this.formBuilder.group({
      agent_name: ['', Validators.required],
      agent_surname: ['', Validators.required],
      agent_phone: ['', Validators.required],
      agent_address: '',
      agent_gender: '',
      agent_date_of_birth: '',
      agent_work_place: ''
    });
  }

  get f() {
    return this.editAgentForm.controls;
  }

}
