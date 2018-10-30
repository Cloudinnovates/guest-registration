import { Component, OnInit, ViewChild } from '@angular/core';
import{GuestService}  from '../../services/guest.service';
import{Guest}  from '../../classes/guest';
import{Router}  from '@angular/router';
import { Observable } from 'rxjs';
import {DataSource} from '@angular/cdk/collections';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-guests',
  templateUrl: './guests.component.html',
  styleUrls: ['./guests.component.css']
})

export class GuestsComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  private guests: Guest[];
  private searchTerm: String; 

  dataSource;
  private displayedColumns = ['firstName', 'lastName', 'visitType', 'arrived', 'left',
                              'cardNumber', 'location', 'manager', 'comment', 'operations'];   


  constructor(private _guestService: GuestService, private _router: Router) { }

  ngOnInit() {
    this._guestService.getGuests().subscribe(results => {
      if (!results) {
        return;
      }
      this.dataSource = new MatTableDataSource(results);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    },
      err => {
        if (err['status'] == 401) {
          this._router.navigate(['/auth-error']);
        } else {
          console.error(err);
        }
      }

    )
  }

  search(filterValue: string) {
    this._guestService.getSearchedGuests(filterValue).subscribe(results => {
      if (!results) {
        return;
      }
      this.dataSource = new MatTableDataSource(results);
    },
      err => {
        if (err['status'] == 401) {
          this._router.navigate(['/auth-error']);
        } else {
          console.error(err);
        }
      }
    )

    if (!filterValue) this.ngOnInit();
  }

  deleteGuest(guest) {
    this._guestService.deleteGuest(guest.id).subscribe((results) => {
      this.guests.splice(this.guests.indexOf(guest), 1);
    }, err => {
      if (err['status'] == 401) {
        this._router.navigate(['/auth-error']);
      } else {
        console.error(err);
      }
    });
    this.dataSource.data = this.dataSource.data.filter(i => i !== guest)
  }

  updateGuest(guest) {
    this._guestService.setterGuest(guest);        
    this._router.navigate(['/guest-edit']);  

  }

  newGuest() {
    let guest = new Guest();
    this._guestService.setterGuest(guest);
    this._router.navigate(['/guest-form']);

  }

  clickMethod(guest) {
    if (confirm("Are you sure you want to delete guest: " + guest.firstName + "?")) {  
      this.deleteGuest(guest);
    }

  }

}
