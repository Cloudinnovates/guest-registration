import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PasteventComponent } from './pastevent.component';

describe('PasteventComponent', () => {
  let component: PasteventComponent;
  let fixture: ComponentFixture<PasteventComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PasteventComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasteventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
