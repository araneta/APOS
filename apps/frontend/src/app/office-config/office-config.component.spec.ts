import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeConfigComponent } from './office-config.component';

describe('OfficeConfigComponent', () => {
  let component: OfficeConfigComponent;
  let fixture: ComponentFixture<OfficeConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OfficeConfigComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OfficeConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
