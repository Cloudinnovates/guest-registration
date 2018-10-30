import { OfficesListComponent } from './offices_section/offices_list/offices-list.component';
import { CreateOfficeComponent } from './offices_section/new_office/create-office.component';
import { EditOfficeComponent } from './offices_section/edit_office/edit-office.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CardListComponent } from './cards_section/card-list/card-list.component';
import { CardHistoryComponent } from './components/card-history/card-history.component'; 
import { NewCardComponent } from './cards_section/new-card/new-card.component';
import { EditCardComponent } from './cards_section/edit-card/edit-card.component';
import { ListeventComponent } from './components/listevent/listevent.component';
import { PasteventComponent } from './components/pastevent/pastevent.component';
import { EventFormComponent } from './components/event-form/event-form.component';
import { GuestFormComponent } from './components/guest-form/guest-form.component';
import { GuestsComponent } from './components/guests/guests.component';
import { EventDetailsComponent } from './components/event-details/event-details.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent} from './sign-up/sign-up.component';
import { ErrorComponent } from './error/error.component';
import { AgentsListComponent } from './agents_section/agents_list/agents-list.component';
import { GuestEditComponent } from './components/guest-edit/guest-edit.component'; 
import { CreateAgentComponent } from './agents_section/new_agent/create-agent.component';
import { EditAgentComponent } from './agents_section/edit_agent/edit-agent.component';
import { EmployeesListComponent } from './employees_section/employees_list/employees-list.component';
import { CreateEmployeeComponent } from './employees_section/new_employee/create-employee.component';
import { EditEmployeeComponent } from './employees_section/edit-employee/edit-employee.component';
import { AllEmployeesComponent } from './employees_section/all_employees/all-employees.component';
import { NewEmployeeComponent } from './employees_section/new_employee/new-employee.component';
import { UpdateEmployeeComponent } from './employees_section/edit-employee/update-employee.component';
import { HomeComponent } from './components/home/home.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'signup',
    component: SignUpComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'cards',
    component: CardListComponent
  },
  {
    path: 'card-history',
    component: CardHistoryComponent   
  },
  {
    path: 'cards/new_card',
    component: NewCardComponent
  },
  {
    path: 'cards/edit/:id',
    component: EditCardComponent
  },
  {
    path: 'upcoming-events',
    component: ListeventComponent
  },
  {
    path: 'past-events',
    component: PasteventComponent
  },
  {
    path: 'event-form',
    component: EventFormComponent
  },
  {
    path: 'guest-form',
    component: GuestFormComponent
  },
  {
    path: 'guest-edit',
    component: GuestEditComponent
  },
  {
    path: 'guest-registry',
    component: GuestsComponent
  },
  {
    path: 'event-details/:id',
    component: EventDetailsComponent
  },
  {
    path: 'event-checkin/:id',
    component: EventDetailsComponent
  },
  { 
    path: 'offices',
    component: OfficesListComponent
  },
  {
    path: 'offices/add',
    component: CreateOfficeComponent
  },
  {
    path: 'offices/edit',
    component: EditOfficeComponent
  },
  {
    path: 'auth-error',
    component: ErrorComponent
  },
  {
    path: 'agents',
    component: AgentsListComponent
  },
  {
    path: 'agents/add',
    component: CreateAgentComponent
  },
  {
    path: 'agents/edit',
    component: EditAgentComponent
  },
  {
    path: 'office/:id',
    component: EmployeesListComponent
  },
  {
    path: 'employee/add',
    component: CreateEmployeeComponent
  },
  {
    path: 'employees/edit',
    component: EditEmployeeComponent
  },
  {
    path: 'employees',
    component: AllEmployeesComponent
  },
  {
    path: 'employee/new',
    component: NewEmployeeComponent
  },
  {
    path: 'employee/update',
    component: UpdateEmployeeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
