<template>
  <div class="auth-container">
    <div class="auth-card" :class="{ 'expanded': showSignup }">
      <!-- Show Login Form -->
      <div v-if="!showSignup" class="form-container">
        <div class="logo-section">
          <div class="logo">
            <i class="fas fa-gamepad"></i>
          </div>
          <h1>Welcome Back!</h1>
          <p class="subtitle">Sign in to continue to your account</p>
        </div>

        <!-- Login Form -->
        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label for="login-email">
              <i class="fas fa-envelope"></i>
              Email
            </label>
            <div class="input-wrapper">
              <input
                  type="email"
                  id="login-email"
                  v-model="loginForm.email"
                  :class="{ 'error': loginErrors.email }"
                  placeholder="Enter your email"
                  autocomplete="email"
              />
              <label for="login-name">
                <i class="fas fa-user"></i>
                User Name
              </label>
              <input
                  type="text"
                  id="login-name"
                  v-model="loginForm.name"
                  :class="{ 'error': loginErrors.name }"
                  placeholder="Enter your name"
                  autocomplete="name"
              />
            </div>
            <span class="error-message" v-if="loginErrors.email">{{ loginErrors.email }}</span>
          </div>

          <div class="form-group">
            <div class="password-label">
              <label for="login-password">
                <i class="fas fa-lock"></i>
                Password
              </label>
              <a href="#" @click.prevent="forgotPassword" class="forgot-password">
                Forgot Password?
              </a>
            </div>
            <div class="input-wrapper">
              <input
                  :type="showLoginPassword ? 'text' : 'password'"
                  id="login-password"
                  v-model="loginForm.password"
                  :class="{ 'error': loginErrors.password }"
                  placeholder="Enter your password"
                  autocomplete="current-password"
              />
              <button
                  type="button"
                  class="toggle-password"
                  @click="showLoginPassword = !showLoginPassword"
              >
                <i :class="showLoginPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
              </button>
            </div>
            <span class="error-message" v-if="loginErrors.password">{{ loginErrors.password }}</span>
          </div>

          <div class="remember-me">
            <label class="checkbox-label">
              <input type="checkbox" v-model="loginForm.rememberMe" />
              <span class="custom-checkbox"></span>
              Remember me
            </label>
          </div>

          <button type="submit" class="btn-primary" :disabled="isLoggingIn">
            <span v-if="!isLoggingIn">
              Sign In
              <i class="fas fa-sign-in-alt"></i>
            </span>
            <div class="spinner" v-else></div>
          </button>

          <div class="divider">
            <span>Don't have an account?</span>
          </div>

          <button type="button" @click="toggleSignup" class="btn-secondary">
            Create Account
            <i class="fas fa-user-plus"></i>
          </button>
        </form>
      </div>

      <!-- Show Create Account Form -->
      <transition name="fade">
        <div v-if="showSignup" class="form-container">
          <div class="back-button" @click="toggleSignup">
            <i class="fas fa-arrow-left"></i>
            Back to Login
          </div>
          <CreateAccountForm @registration-success="handleRegistrationSuccess" />
        </div>
      </transition>

      <!-- Error Modal -->
      <div v-if="showErrorModal" class="modal" @click.self="closeErrorModal">
        <div class="modal-content error-modal">
          <div class="modal-icon error">
            <i class="fas fa-exclamation-circle"></i>
          </div>
          <h3>Login Failed</h3>
          <p>{{ errorMessage }}</p>
          <button @click="closeErrorModal" class="btn-primary">
            <i class="fas fa-times"></i>
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CreateAccountForm from './CreateAccountForm.vue'
import axios from "axios";
const axiosClient = axios.create({
  // NOTE: it's baseURL, not baseUrl
  baseURL: "http://localhost:8080"
});
export default {
  name: 'LoginPage',
  components: {
    CreateAccountForm
  },
  data() {
    return {
      loginForm: {
        name: '',
        email: '',
        password: '',
        rememberMe: false
      },

      loginErrors: {},
      isLoggingIn: false,
      showLoginPassword: false,
      showErrorModal: false,
      errorMessage: '',
      showSignup: false
    }
  },
  methods: {
    toggleSignup() {
      this.showSignup = !this.showSignup;
      this.loginErrors = {};
      this.loginForm = {
        email: '',
        password: '',
        rememberMe: false
      };
    },

    handleRegistrationSuccess() {
      this.showSignup = false;
      // Optionally pre-fill the login form with the registered email
      this.loginForm.email = this.registeredEmail;
      // Show success message
      this.showSuccess('Account created successfully! Please sign in.');
    },

    async handleLogin() {
      this.loginErrors = {};

      // Validate form
      if (!this.loginForm.email) {
        this.loginErrors.email = 'Email is required';
      }
      if (!this.loginForm.password) {
        this.loginErrors.password = 'Password is required';
      }

      if (Object.keys(this.loginErrors).length > 0) {
        return;
      }

      this.isLoggingIn = true;

      try {
        // Implement your login API call here
        const response = await this.loginUser(this.loginForm);

        if (response.success) {
          // Handle successful login
          this.$router.push('/dashboard'); // or wherever you want to redirect
        } else {
          this.showError('Invalid email or password');
        }
      } catch (error) {
        this.showError('An error occurred during login. Please try again.');
        console.error('Login error:', error);
      } finally {
        this.isLoggingIn = false;
      }
    },

    async loginUser() {
      this.person={
        name:this.loginForm.name,
        email:this.loginForm.email,
        password:this.loginForm.password,
      };
      // Implement your login API call here
      const ownerResponse = await axiosClient.get(`/api/owners`)
      let employeeResponse = null;
      try { employeeResponse = await axiosClient.get(`/api/employees/${this.person.email}`)}
      catch (e) {
      }
      // console.log("employee data:", employeeResponse.data)
      if (ownerResponse.data.email === this.person.email) {
        try{
          // this.person.name = ownerResponse.data.name;
          await axiosClient.post(`/api/owners/owner/login`, this.person)
          sessionStorage.setItem('owner', JSON.stringify(this.person));
        }catch(error){
          console.error("Error encountering when logging in:", error);
        }
      }else if(employeeResponse && employeeResponse.data.email === this.person.email) {
        try{
          // this.person.name = employeeResponse.data.name;
          await axiosClient.post(`/api/employees/employees/login`, this.person)
          sessionStorage.setItem('employee', JSON.stringify(this.person));
        }catch(error){
          console.error("Error encountering when logging in:", error);
        }
      }else{
        try{
          await axiosClient.post(`/customers/login`, this.person)
          console.log(this.person)
          sessionStorage.setItem('customer', JSON.stringify(this.person))
        }catch(error){
          console.error("Error encountering when logging in:", error);
        }
      }



      window.location.href = "/";
      return { success: true }; // Placeholder
    },

    showSuccess(message) {
      // Create the message container dynamically
      const successMessage = document.createElement("div");
      successMessage.className = "success-message";
      successMessage.textContent = message;

      // Append the message to the auth container
      const authContainer = document.querySelector(".auth-container");
      authContainer.appendChild(successMessage);

      // Set timeout to fade out and remove the message
      setTimeout(() => {
        successMessage.style.opacity = 0; // Start fading out
        setTimeout(() => successMessage.remove(), 1000); // Remove after fade-out
      }, 3000); // Display for 3 seconds before fading out
    },

    forgotPassword() {
      // Implement forgot password flow
    },

    showError(message) {
      this.errorMessage = message;
      this.showErrorModal = true;
    },

    closeErrorModal() {
      this.showErrorModal = false;
      this.errorMessage = '';
    }
  }
}
</script>

<style scoped>
/* Previous styles remain the same */

/* Add these new styles */
.auth-card {
  transition: all 0.3s ease;
}

.auth-card.expanded {
  max-width: 600px; /* Wider card for the registration form */
}

.divider span {
    color:black;
    font-weight:bold;
}

.form-container {
  position: relative;
}

label {
  color: black; /* Ensures all labels are black */
  font-weight: bold; /* Optional: makes labels bold */
}

.logo-section h1 {
  font-size: 30px;
  margin-bottom: 10px;
  color:black;
}

.back-button {
  position: absolute;
  top: -20px;
  left: 0;
  color: #6b46c1;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  transition: color 0.3s ease;
}

.back-button:hover {
  color: #553c9a;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>