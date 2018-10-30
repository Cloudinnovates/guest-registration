import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Agent } from '../../classes/agent';
import { AgentService } from '../../services/agent.service';
import {DatePipe} from '@angular/common';
import {DuplicateErrorComponent} from '../../shared/error-window/duplicate-error.component';
import {MatDialog} from '@angular/material';


@Component ({
  selector: 'create-agent',
  templateUrl: './create-agent.component.html',
  styleUrls: ['./create-agent.component.css']
})
export class CreateAgentComponent implements OnInit {
  newAgentForm : FormGroup;
  submitted = false;
  agent: Agent = new Agent();
  agents: Agent[];

  constructor(private agentService: AgentService, private formBuilder : FormBuilder, private router: Router, private dialog: MatDialog) {
  }

  createForm() {
    this.newAgentForm = this.formBuilder.group({
      agent_name: ['', Validators.required],
      agent_surname: ['', Validators.required],
      agent_phone: ['', Validators.required],
      agent_address: '',
      agent_gender: '',
      agent_date_of_birth: '',
      agent_work_place: ''
    });
  }

  ngOnInit() {
    this.createForm();
  }

  get f() {
    return this.newAgentForm.controls;
  }

  createAgent(name, surname, phone, address, gender, date_of_birth, work_place) {
    this.submitted = true;

    // stop here if form is invalid
    if (this.newAgentForm.invalid) {
      return;
    }

    this.agentService.checkAgent(name, surname).subscribe(data => {  
      if (data == true) {
        this.dialog.open(DuplicateErrorComponent, {width:'360px'});
        return;
      } else {
          this.agent.name = name;
          this.agent.surname = surname;
          this.agent.phone = phone;

          if (address) {
            this.agent.address = address;
          }

          if (gender) {
            this.agent.gender = gender;
          }

          let date = new Date(new DatePipe('en-US').transform(date_of_birth, 'MM-dd-yyyy') + " 12:00:00")

          if (date_of_birth) {
            this.agent.date_of_birth = date.getTime();
          }

          if (work_place) {
            this.agent.work_place = work_place;
          }

          this.agentService.createAgent(this.agent)
            .subscribe((data) => {this.router.navigate(['/agents'])}, err => {
                if (err['status'] == 401){
                  this.router.navigate(['/auth-error']);
                }else{
                  console.error(err);
                }
              },
              () => console.log("Done"));
        }

    });
  }

}
