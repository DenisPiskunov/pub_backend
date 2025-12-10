import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AndroidMarketDataComponent } from './android-market-data.component';

describe('AndroidMarketDataComponent', () => {
  let component: AndroidMarketDataComponent;
  let fixture: ComponentFixture<AndroidMarketDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AndroidMarketDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AndroidMarketDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
