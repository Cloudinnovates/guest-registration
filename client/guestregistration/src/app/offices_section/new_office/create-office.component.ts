import { Component, OnInit } from '@angular/core';

import {Office} from '../../classes/offices';
import {OfficeService} from '../../services/office.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';


@Component ({
  selector: 'create-office',
  templateUrl: './create-office.component.html',
  styleUrls: ['./create-office.component.css']
})
export class CreateOfficeComponent implements OnInit {
  newOfficeForm : FormGroup;
  submitted = false;
  office: Office = new Office();
  offices: Office[];

  constructor(private officeService: OfficeService, private formBuilder : FormBuilder, private router: Router) {
  }

  createForm() {
    this.newOfficeForm = this.formBuilder.group({
      office_name: ['', Validators.required],
      office_address: ['', Validators.required],
      office_phone: ['', Validators.required],
      office_email: ['', Validators.required],
      // office_type: ['', Validators.required],
      office_type: '',
      office_manager_name: ['', Validators.required],
      // office_manager_surname: ['', Validators.required],
      office_manager_surname: '',
      office_manager_email: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.createForm();
  }

  get f() {
    return this.newOfficeForm.controls;
  }

  createOffice(name, address, phone, email, type, manager_name, manager_surname, manager_email) {
    this.submitted = true;

    // stop here if form is invalid
    if (this.newOfficeForm.invalid) {
      return;
    }

    this.office.name = name;
    this.office.address = address;
    this.office.phone = phone;
    this.office.email = email;
    this.office.type = type;
    this.office.manager_name = manager_name;
    this.office.manager_surname = manager_surname;
    this.office.manager_email= manager_email;
    this.officeService.createOffice(this.office)
      .subscribe((data) => {this.router.navigate(['/offices'])}, err => {
        if (err['status'] == 401){
          this.router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      },
      () => console.log("Done"));

  }

}
