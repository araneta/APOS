import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideToastr } from 'ngx-toastr';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AuthInterceptorService } from './services/auth-interceptor';
import { BASE_PATH } from './api/variables';
import { Configuration } from './api/configuration';
import Aura from '@primeuix/themes/aura';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
export const appConfig: ApplicationConfig = {
  providers: [
     provideAnimationsAsync(),
        providePrimeNG({
            theme: {
                preset: Aura
            }
        }),
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(withInterceptors([AuthInterceptorService])),
    { provide: BASE_PATH, useValue: 'http://localhost:8888' },
    { provide: Configuration, useValue: new Configuration({ basePath: 'http://localhost:8888' }) },
    provideAnimations(),
    provideToastr({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    })
  ]
};
