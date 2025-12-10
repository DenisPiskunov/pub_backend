import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppsGridComponent } from './apps-grid.component';

describe('AppsGridComponent', () => {
  let component: AppsGridComponent;
  let fixture: ComponentFixture<AppsGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppsGridComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppsGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
