import { Component, OnInit, ViewChild } from '@angular/core';
import { OfficeService } from '../../services/office.service';
import { Office } from '../../classes/offices';
import {MatDialog, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {DialogComponent} from '../../shared/dialog-window/dialog.component';
import {Router} from '@angular/router';

@Component({
  selector: 'offices-list',
  templateUrl: './offices-list.component.html',
  styleUrls: ['./offices-list.component.css']
})

export class OfficesListComponent implements OnInit {
  offices: Office[];
  editing_office_id: string;
  searchField: string;
  dataSource: MatTableDataSource<Office>;

  displayedColumns: string[] = ['name', 'address', 'phone', 'email', 'type', 'manager', 'manager_email', 'actions'];


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private officeService: OfficeService, public dialog: MatDialog, private router: Router) {}

  ngOnInit(): void {
    this.getOffices();
    this.officeService.currentMessage.subscribe(editing_office_id => this.editing_office_id = editing_office_id);
  }

  getOffices(): void {
    this.officeService.getOffices()
      .subscribe(offices => {
        this.offices = [];
        Object.keys(offices).forEach(key => {
          this.offices.push(offices[key]);
        });
          this.dataSource = new MatTableDataSource(this.offices);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
      },
      err => {
        if (err['status'] == 401){
          this.router.navigate(['/auth-error']);
        }else{
          console.error(err);
        }
      });
  }


  deleteOffice(office : Office, index: number): void {
    let dialogRef = this.dialog.open(DialogComponent, {width:'270px'});

    dialogRef.afterClosed().subscribe(data => {
      if (data == true) {
        this.offices = this.offices.filter(h => h !== office);
        this.officeService.deleteOffice(office.id).subscribe();
        this.dataSource = new MatTableDataSource(this.offices);
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

  editingOfficeId(id : string) {
    this.officeService.changeMessage(id);
  }

  searchInput() {
    this.dataSource = null;
    if (this.searchField.length > 0) {
      this.officeService.findOffices(this.searchField)
        .subscribe(data => {
          if(data){
            this.offices = [];
            Object.keys(data).forEach(key => {
              this.offices.push(data[key]);
              this.dataSource = new MatTableDataSource(this.offices);
              this.dataSource.paginator = this.paginator;
              this.dataSource.sort = this.sort;
            })
          }
        },
        err => {
          if (err['status'] == 401){
            this.router.navigate(['/auth-error']);
          }else{
            console.error(err);
          }
        }
        );
    }else {
      this.getOffices();
    }
  }

  officeEmployees(id: string){
    this.router.navigate(['/office/'+id])
  }

}
