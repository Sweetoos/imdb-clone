import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TitleCardComponent } from './title-card';

describe('TitleCard', () => {
  let component: TitleCardComponent;
  let fixture: ComponentFixture<TitleCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TitleCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TitleCardComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
