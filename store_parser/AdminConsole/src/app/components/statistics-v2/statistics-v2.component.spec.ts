import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticsV2Component } from './statistics-v2.component';

describe('StatisticsV2Component', () => {
  let component: StatisticsV2Component;
  let fixture: ComponentFixture<StatisticsV2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatisticsV2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatisticsV2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
