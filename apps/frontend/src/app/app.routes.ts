import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfileComponent } from './profile/profile.component';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';
import { OfficeConfigComponent } from './office-config/office-config.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'about', component: AboutComponent },
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { 
        path: 'dashboard', 
        component: DashboardComponent,
        canActivate: [() => {
            const authService = inject(AuthService);
            if (!authService.isAuthenticated()) {
                return false;
            }
            return true;
        }]
    },
    {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [() => {
            const authService = inject(AuthService);
            if (!authService.isAuthenticated()) {
                return false;
            }
            return true;
        }]
    },
    {
        path: 'office-config',
        component: OfficeConfigComponent,
        canActivate: [() => {
            const authService = inject(AuthService);
            if (!authService.isAuthenticated()) {
                return false;   
            }
            return true;
        }]
    }
];
