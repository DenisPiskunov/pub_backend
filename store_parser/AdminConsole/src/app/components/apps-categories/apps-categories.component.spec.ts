import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppsCategoriesComponent } from './apps-categories.component';

describe('AppsCategoriesComponent', () => {
  let component: AppsCategoriesComponent;
  let fixture: ComponentFixture<AppsCategoriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppsCategoriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppsCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
