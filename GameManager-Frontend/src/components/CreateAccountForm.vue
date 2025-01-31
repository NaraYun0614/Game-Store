<template>
  <div class="create-account-section">
    <!-- Header -->
    <div class="form-header">
      <div class="logo-wrapper">
        <div class="logo-bg-circle"></div>
        <div class="logo">
          <i class="fas fa-gamepad"></i>
        </div>
      </div>
      <h2>Create Your Account</h2>
      <p class="subtitle">Join our gaming community today</p>
    </div>

    <!-- Progress Steps -->
    <div class="progress-steps">
      <div class="step" :class="{ 'active': currentStep === 1, 'completed': currentStep > 1 }">
        <div class="step-number">
          <i v-if="currentStep > 1" class="fas fa-check"></i>
          <span v-else>1</span>
        </div>
        <span class="step-label">Account</span>
      </div>
      <div class="step-line"></div>
      <div class="step" :class="{ 'active': currentStep === 2, 'completed': currentStep > 2 }">
        <div class="step-number">
          <i v-if="currentStep > 2" class="fas fa-check"></i>
          <span v-else>2</span>
        </div>
        <span class="step-label">Personal</span>
      </div>
      <div class="step-line"></div>
      <div class="step" :class="{ 'active': currentStep === 3, 'completed': currentStep > 3 }">
        <div class="step-number">
          <i v-if="currentStep > 3" class="fas fa-check"></i>
          <span v-else>3</span>
        </div>
        <span class="step-label">Complete</span>
      </div>
    </div>

    <!-- Background Animations -->
    <div class="background-animations">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
      <div class="square square-1"></div>
      <div class="square square-2"></div>
    </div>

    <!-- Main Form -->
    <form @submit.prevent="handleSubmit" class="signup-form">
      <!-- Step 1: Account Details -->
      <div v-if="currentStep === 1" class="form-step">
        <div class="form-group">
          <label for="email">
            <i class="fas fa-envelope"></i>
            Email Address
          </label>
          <div class="input-wrapper">
            <input
                type="email"
                id="email"
                v-model="formData.email"
                :class="{ 'error': errors.email }"
                placeholder="your.email@example.com"
            />
            <span class="input-focus"></span>
            <div class="input-icon" v-if="formData.email && !errors.email">
              <i class="fas fa-check-circle"></i>
            </div>
          </div>
          <span class="error-message" v-if="errors.email">
            <i class="fas fa-exclamation-circle"></i>
            {{ errors.email }}
          </span>
        </div>

        <div class="form-group">
          <label for="username">
            <i class="fas fa-user"></i>
            Username
          </label>
          <div class="input-wrapper">
            <input
                type="text"
                id="username"
                v-model="formData.username"
                :class="{ 'error': errors.username }"
                placeholder="Choose your gaming handle"
            />
            <span class="input-focus"></span>
          </div>
          <span class="error-message" v-if="errors.username">
            <i class="fas fa-exclamation-circle"></i>
            {{ errors.username }}
          </span>
        </div>

        <div class="form-group">
          <label for="password">
            <i class="fas fa-lock"></i>
            Password
          </label>
          <div class="input-wrapper">
            <input
                :type="showPassword ? 'text' : 'password'"
                id="password"
                v-model="formData.password"
                :class="{ 'error': errors.password }"
                placeholder="Create a strong password"
            />
            <span class="input-focus"></span>
            <button
                type="button"
                class="toggle-password"
                @click="showPassword = !showPassword"
            >
              <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </button>
          </div>
          <div class="password-strength" v-if="formData.password">
            <div class="strength-meter">
              <div
                  class="strength-bar"
                  :style="{ width: passwordStrength.score * 25 + '%' }"
                  :class="passwordStrength.class"
              ></div>
            </div>
            <span :class="'strength-text ' + passwordStrength.class">
              {{ passwordStrength.label }}
            </span>
          </div>
        </div>
      </div>

      <!-- Step 2: Personal Details -->
      <div v-if="currentStep === 2" class="form-step">
        <div class="form-group">
          <label for="address">
            <i class="fas fa-home"></i>
            Delivery Address
          </label>
          <div class="input-wrapper">
            <textarea
                id="address"
                v-model="formData.address"
                :class="{ 'error': errors.address }"
                placeholder="Enter your delivery address"
                rows="3"
            ></textarea>
            <span class="input-focus"></span>
          </div>
          <span class="error-message" v-if="errors.address">
            <i class="fas fa-exclamation-circle"></i>
            {{ errors.address }}
          </span>
        </div>

        <div class="form-row">
          <div class="form-group half">
            <label for="postcode">
              <i class="fas fa-map-marker-alt"></i>
              Postcode
            </label>
            <div class="input-wrapper">
              <input
                  type="text"
                  id="postcode"
                  v-model="formData.postcode"
                  :class="{ 'error': errors.postcode }"
                  placeholder="Enter postcode"
              />
              <span class="input-focus"></span>
            </div>
          </div>

          <div class="form-group half">
            <label for="phone">
              <i class="fas fa-phone"></i>
              Phone Number
            </label>
            <div class="input-wrapper">
              <input
                  type="tel"
                  id="phone"
                  v-model="formData.phone"
                  :class="{ 'error': errors.phone }"
                  placeholder="Enter phone number"
              />
              <span class="input-focus"></span>
            </div>
          </div>
        </div>

        <div class="form-group">
          <label for="age">
            <i class="fas fa-birthday-cake"></i>
            Age
          </label>
          <div class="input-wrapper">
            <input
                type="number"
                id="age"
                v-model.number="formData.age"
                :class="{ 'error': errors.age }"
                placeholder="Enter your age"
                min="0"
                max="120"
            />
            <span class="input-focus"></span>
          </div>
          <span class="error-message" v-if="errors.age">
            <i class="fas fa-exclamation-circle"></i>
            {{ errors.age }}
          </span>
        </div>
      </div>

      <!-- Step 3: Confirmation -->
      <div v-if="currentStep === 3" class="form-step">
        <div class="confirmation-container">
          <div class="terms-section">
            <h3>
              <i class="fas fa-shield-alt"></i>
              Terms & Privacy
            </h3>
            <div class="terms-box">
              <div class="terms-scroll">
                <h4>Terms of Service</h4>
                <p>Welcome to our gaming platform. By creating an account, you agree to:</p>
                <ul>
                  <li>Provide accurate and complete information</li>
                  <li>Maintain the security of your account</li>
                  <li>Respect other users and follow community guidelines</li>
                  <li>Comply with all applicable laws and regulations</li>
                </ul>
              </div>
            </div>
            <label class="checkbox-label">
              <input
                  type="checkbox"
                  v-model="formData.termsAccepted"
                  :class="{ 'error': errors.termsAccepted }"
              />
              <span class="custom-checkbox">
                <i class="fas fa-check"></i>
              </span>
              I accept the Terms and Conditions and Privacy Policy
            </label>
            <span class="error-message" v-if="errors.termsAccepted">
              <i class="fas fa-exclamation-circle"></i>
              {{ errors.termsAccepted }}
            </span>
          </div>

          <div class="summary-section">
            <h3>
              <i class="fas fa-clipboard-check"></i>
              Account Summary
            </h3>
            <div class="summary-box">
              <div class="summary-item">
                <span class="label">Email:</span>
                <span class="value">{{ formData.email }}</span>
              </div>
              <div class="summary-item">
                <span class="label">Username:</span>
                <span class="value">{{ formData.username }}</span>
              </div>
              <div class="summary-item">
                <span class="label">Address:</span>
                <span class="value">{{ formData.address }}</span>
              </div>
              <div class="summary-item">
                <span class="label">Postcode:</span>
                <span class="value">{{ formData.postcode }}</span>
              </div>
              <div class="summary-item">
                <span class="label">Phone:</span>
                <span class="value">{{ formData.phone }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Navigation Buttons -->
      <div class="form-navigation">
        <button
            type="button"
            class="btn-secondary"
            @click="prevStep"
            v-if="currentStep > 1"
        >
          <i class="fas fa-arrow-left"></i>
          Back
        </button>

        <button
            type="button"
            class="btn-primary"
            @click="nextStep"
            v-if="currentStep < 3"
        >
          Next
          <i class="fas fa-arrow-right"></i>
        </button>

        <button
            type="submit"
            class="btn-primary submit-btn"
            v-if="currentStep === 3"
            :disabled="isSubmitting"
        >
          <span v-if="!isSubmitting">
            Create Account
            <i class="fas fa-user-plus"></i>
          </span>
          <div class="spinner" v-else></div>
        </button>
      </div>
    </form>

    <!-- Success Modal -->
    <div v-if="showSuccessModal" class="modal" @click.self="closeSuccessModal">
      <div class="modal-content success-modal">
        <div class="modal-icon success">
          <i class="fas fa-check-circle"></i>
        </div>
        <h3>Registration Successful!</h3>
        <p>Your account has been created successfully.</p>
        <button @click="closeSuccessModal" class="btn-primary">
          <i class="fas fa-sign-in-alt"></i>
          Proceed to Login
        </button>
      </div>
    </div>

    <!-- Error Modal -->
    <div v-if="showErrorModal" class="modal" @click.self="closeErrorModal">
      <div class="modal-content error-modal">
        <div class="modal-icon error">
          <i class="fas fa-exclamation-circle"></i>
        </div>
        <h3>Registration Failed</h3>
        <p>{{ errorMessage }}</p>
        <button @click="closeErrorModal" class="btn-primary">
          <i class="fas fa-times"></i>
          Close
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
const axiosClient = axios.create({
  // NOTE: it's baseURL, not baseUrl
  baseURL: "http://localhost:8080"
});
export default {
  name: 'CreateAccountForm',
  data() {
    return {
      currentStep: 1,
      showPassword: false,
      isSubmitting: false,
      showSuccessModal: false,
      showErrorModal: false,
      errorMessage: '',
      formData: {
        email: '',
        username: '',
        password: '',
        address: '',
        postcode: '',
        phone: '',
        age: null,
        termsAccepted: false
      },
      errors: {},
    }
  },
  computed: {
    passwordStrength() {
      if (!this.formData.password) {
        return { score: 0, label: '', class: '' }
      }

      const password = this.formData.password
      let score = 0

      if (password.length >= 8) score++
      if (/[A-Z]/.test(password)) score++
      if (/[0-9]/.test(password)) score++
      if (/[^A-Za-z0-9]/.test(password)) score++

      const strengths = [
        { score: 0, label: 'Very Weak', class: 'very-weak' },
        { score: 1, label: 'Weak', class: 'weak' },
        { score: 2, label: 'Medium', class: 'medium' },
        { score: 3, label: 'Strong', class: 'strong' },
        { score: 4, label: 'Very Strong', class: 'very-strong' }
      ]

      return strengths[score]
    }
  },
  methods: {
    validateStep() {
      this.errors = {}

      if (this.currentStep === 1) {
        if (!this.formData.email) this.errors.email = 'Email is required'
        else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.formData.email)) {
          this.errors.email = 'Please enter a valid email address'
        }

        if (!this.formData.username) this.errors.username = 'Username is required'
        if (!this.formData.password) this.errors.password = 'Password is required'
        else if (this.formData.password.length < 8) {
          this.errors.password = 'Password must be at least 8 characters'
        }
      }

      if (this.currentStep === 2) {
        if (!this.formData.address) this.errors.address = 'Address is required'
        if (!this.formData.postcode) this.errors.postcode = 'Postcode is required'
        if (!this.formData.phone) this.errors.phone = 'Phone number is required'
        if (!this.formData.age) this.errors.age = 'Age is required'
        else if (this.formData.age < 13) {
          this.errors.age = 'You must be at least 13 years old'
        }
      }

      if (this.currentStep === 3) {
        if (!this.formData.termsAccepted) {
          this.errors.termsAccepted = 'You must accept the terms and conditions'
        }
      }

      return Object.keys(this.errors).length === 0
    },

    nextStep() {
      if (this.validateStep()) {
        this.currentStep++
      }
    },

    prevStep() {
      this.currentStep--
    },

    async handleSubmit() {

      this.customer={
        name:this.formData.username,
        email:this.formData.email,
        password:this.formData.password,
      };
      this.isSubmitting = true
      try {

        const response = await axiosClient.post(`/customers`, this.customer)
        // Implement your registration API call here

        this.showSuccessModal = true
        this.$emit('registration-success')

      }
      catch (error) {
        console.error('Registration error:', error)
        this.errorMessage = 'An error occurred during registration. Please try again.'

        this.showErrorModal = true
      }

      finally {
        this.isSubmitting = false
      }
    },

    closeSuccessModal() {
      this.showSuccessModal = false
      this.$router.push('/login') // Redirect to login page

    },

    closeErrorModal() {
      this.showErrorModal = false
      this.errorMessage = ''
    }
  }
}
</script>

<style scoped>
.create-account-section {
  position: relative;
  max-width: 600px;
  margin: 0 auto;
  padding: 40px 20px;
  color:black;
}

/* Background Animations */
.background-animations {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: -1;
  pointer-events: none;
}

.circle, .square {
  position: absolute;
  opacity: 0.1;
}

.circle-1 {
  width: 300px;
  height: 300px;
  background: linear-gradient(45deg, #6b46c1, #805ad5);
  border-radius: 50%;
  top: -150px;
  right: -150px;
  animation: float 8s infinite ease-in-out;
}

.circle-2 {
  width: 200px;
  height: 200px;
  background: linear-gradient(45deg, #805ad5, #6b46c1);
  border-radius: 50%;
  bottom: -100px;
  left: -100px;
  animation: float 6s infinite ease-in-out;
}

.circle-3 {
  width: 150px;
  height: 150px;
  background: linear-gradient(45deg, #6b46c1, #805ad5);
  border-radius: 50%;
  top: 50%;
  right: -75px;
  animation: float 7s infinite ease-in-out;
}

.square {
  border-radius: 20px;
}

.square-1 {
  width: 100px;
  height: 100px;
  background: linear-gradient(45deg, #6b46c1, #805ad5);
  top: 20%;
  left: -50px;
  animation: rotate 10s infinite linear;
}

.square-2 {
  width: 80px;
  height: 80px;
  background: linear-gradient(45deg, #805ad5, #6b46c1);
  bottom: 20%;
  right: -40px;
  animation: rotate 8s infinite linear reverse;
}

/* Form Header */
.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-wrapper {
  position: relative;
  width: 100px;
  height: 100px;
  margin: 0 auto 20px;
}

.logo {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #6b46c1, #805ad5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 2.5rem;
  position: relative;
  z-index: 1;
}

.logo-bg-circle {
  position: absolute;
  top: 10px;
  left: 10px;
  right: 10px;
  bottom: 10px;
  background: linear-gradient(135deg, #6b46c1, #805ad5);
  border-radius: 50%;
  opacity: 0.3;
  animation: pulse 2s infinite;
}

/* Form Steps */
.form-step {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease-out;
}

/* Input Styles */
.input-wrapper {
  position: relative;
}

input, textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

input:focus, textarea:focus {
  border-color: #6b46c1;
  outline: none;
  box-shadow: 0 0 0 3px rgba(107, 70, 193, 0.1);
}

.input-focus {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(to right, #6b46c1, #805ad5);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

input:focus ~ .input-focus,
textarea:focus ~ .input-focus {
  width: 100%;
}

/* Password Strength */
.password-strength {
  margin-top: 8px;
}

.strength-meter {
  height: 4px;
  background: #e2e8f0;
  border-radius: 2px;
  margin-bottom: 4px;
}

.strength-bar {
  height: 100%;
  border-radius: 2px;
  transition: all 0.3s ease;
}

/* Button Styles */
.form-navigation {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

.btn-primary, .btn-secondary {
  padding: 12px 24px;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-primary {
  background: linear-gradient(to right, #6b46c1, #805ad5);
  color: white;
  border: none;
  flex: 1;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(107, 70, 193, 0.2);
}

.btn-secondary {
  background: white;
  color: #6b46c1;
  border: 2px solid #6b46c1;
}

.btn-secondary:hover {
  background: #f7fafc;
}

/* Modal Styles */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 15px;
  max-width: 400px;
  width: 90%;
  text-align: center;
}

.modal-icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.modal-icon.success {
  color: #48bb78;
}

.modal-icon.error {
  color: #f56565;
}

/* Animations */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.1;
  }
  100% {
    transform: scale(1);
    opacity: 0.3;
  }
}

/* Responsive Styles */
@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
    gap: 20px;
  }

  .btn-primary, .btn-secondary {
    padding: 10px 20px;
    font-size: 0.9rem;
  }

  .form-step {
    padding: 20px;
  }
}
</style>