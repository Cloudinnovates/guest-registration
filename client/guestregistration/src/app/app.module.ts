import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule }   from '@angular/http';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatInputModule, MatPaginatorModule, MatProgressSpinnerModule, MatSortModule,   
        MatTableModule, MatSelectModule, MatDialogModule, MatPaginatorIntl, MatAutocompleteModule } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OfficesListComponent } from './offices_section/offices_list/offices-list.component';
import { OfficeService } from './services/office.service';
import { CreateOfficeComponent } from './offices_section/new_office/create-office.component';
import { EditOfficeComponent } from './offices_section/edit_office/edit-office.component';
import { AppRoutingModule } from './app-routing.module';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { AppComponent } from './app.component';
import { ListeventComponent } from './components/listevent/listevent.component';
import { GuestsComponent } from './components/guests/guests.component';
import { PasteventComponent } from './components/pastevent/pastevent.component';
import { EventFormComponent } from './components/event-form/event-form.component';
import { GuestFormComponent } from './components/guest-form/guest-form.component';
import { EventService }   from './services/event.service';
import { GuestService }   from './services/guest.service';
import { CardHistoryService } from './services/card-history.service'; 
import { CardHistoryComponent } from './components/card-history/card-history.component';  
import { CardListComponent } from './cards_section/card-list/card-list.component';
import { CardService } from './services/card.service';
import { NewCardComponent } from './cards_section/new-card/new-card.component';
import { EditCardComponent } from './cards_section/edit-card/edit-card.component'
import { CustomMaterialModule } from './shared/cutommaterial/cutommaterial.module';
import { DialogComponent } from './shared/dialog-window/dialog.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { ErrorComponent } from './error/error.component';
import { CustomMatPaginatorIntl } from './shared/custom-mat-paginator-int';
import { AgentsListComponent } from './agents_section/agents_list/agents-list.component';
import { AgentService } from './services/agent.service';
import { CreateAgentComponent } from './agents_section/new_agent/create-agent.component';
import { DuplicateErrorComponent } from './shared/error-window/duplicate-error.component';
import { EditAgentComponent } from './agents_section/edit_agent/edit-agent.component';
import { EventDetailsComponent } from './components/event-details/event-details.component'; 
import { GuestEditComponent } from './components/guest-edit/guest-edit.component'; 
import { EmployeeService } from './services/employee.service';
import { EmployeesListComponent } from './employees_section/employees_list/employees-list.component';
import { CreateEmployeeComponent } from './employees_section/new_employee/create-employee.component';
import { EditEmployeeComponent } from './employees_section/edit-employee/edit-employee.component';
import { CardSelectDialog } from './shared/card-dialog/card-select-dialog';
import { AllEmployeesComponent } from './employees_section/all_employees/all-employees.component';
import { NewEmployeeComponent } from './employees_section/new_employee/new-employee.component';
import { UpdateEmployeeComponent } from './employees_section/edit-employee/update-employee.component';
import { HomeComponent } from './components/home/home.component';


export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    ListeventComponent,
    GuestsComponent,
    PasteventComponent,
    EventFormComponent,
    GuestFormComponent,
    CardListComponent,
    CardHistoryComponent,
    NewCardComponent,
    EditCardComponent,
    OfficesListComponent,
    CreateOfficeComponent,
    EditOfficeComponent,
    DialogComponent,
    LoginComponent,
    SignUpComponent,
    ErrorComponent,
    AgentsListComponent,
    CreateAgentComponent,
    EditAgentComponent,
    EventDetailsComponent,
    GuestEditComponent,
    DuplicateErrorComponent,
    EmployeesListComponent,
    CreateEmployeeComponent,
    EditEmployeeComponent,
    EventDetailsComponent,
    CardSelectDialog,
    AllEmployeesComponent,
    NewEmployeeComponent,
    UpdateEmployeeComponent,
    HomeComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpModule,
    HttpClientModule,
    FormsModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatAutocompleteModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatDialogModule,
    ReactiveFormsModule,
    CustomMaterialModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    })
  ],
  providers: [
    EventService,
    GuestService,
    CardService,
    CardHistoryService,
    OfficeService,
    AgentService,
    EmployeeService,
    {
      provide: MatPaginatorIntl,
      useClass: CustomMatPaginatorIntl
    }
  ],
  entryComponents: [
    DialogComponent,
    DuplicateErrorComponent,
    CardSelectDialog
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
