import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BankuserComponent } from './bankuser.component';

describe('BankuserComponent', () => {
  let component: BankuserComponent;
  let fixture: ComponentFixture<BankuserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankuserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BankuserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
