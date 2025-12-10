import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManualAddingComponent } from './manual-adding.component';

describe('ManualAddingComponent', () => {
  let component: ManualAddingComponent;
  let fixture: ComponentFixture<ManualAddingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManualAddingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManualAddingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
