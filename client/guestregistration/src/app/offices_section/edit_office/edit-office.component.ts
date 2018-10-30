import {Component, OnInit} from '@angular/core';

import {Office} from '../../classes/offices';
import {OfficeService} from '../../services/office.service';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component ({
  selector: 'edit-office',
  templateUrl: './edit-office.component.html',
  styleUrls: ['./edit-office.component.css']
})

export class EditOfficeComponent implements OnInit {
  office: Office = new Office;
  editing_office_id: string;
  offices: Office[];
  editOfficeForm: FormGroup;
  submitted = false;

  constructor(private officeService: OfficeService, private formBuilder: FormBuilder, private router: Router) {
  }

  ngOnInit() {
    this.officeService.currentMessage.subscribe(editing_office_id => this.editing_office_id = editing_office_id);
    this.getOffice(this.editing_office_id);
    this.createForm();
  }

  getOffice(id: string): void {
    this.officeService.getOffice(id)
      .subscribe(data => this.office = Object(data),
      err => {
        if (err['status'] == 401){
          this.router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      },
        () => console.log("Completed")
    );
  }

  editOffice(officeData: Office): void {
    this.submitted = true;

    // stop here if form is invalid
    if (this.editOfficeForm.invalid) {
      return;
    }

    this.officeService.updateOffice(officeData)
      .subscribe((data) => {this.router.navigate(['/offices'])}, err => {
        if (err['status'] == 401){
          this.router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      },
        () => console.log("Done"));
  }

  createForm() {
    this.editOfficeForm = this.formBuilder.group({
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

  get f() {
    return this.editOfficeForm.controls;
  }

}
