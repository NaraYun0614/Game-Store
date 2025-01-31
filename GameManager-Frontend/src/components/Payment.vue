<template>
  <body>
  <div class="payment-container">
    <div class="payment-box">
    <h1>Payment Method</h1>
    <p>Add a new payment method to your account.</p>
    <div class="payment-methods">
      <button
          v-for="method in methods"
          :key="method"
          :class="{ selected: selectedMethod === method }"
          @click="selectMethod(method)"
          class="method"
      >
        <span>{{ method }}</span>
      </button>
    </div>

    <form @submit.prevent="submitPayment" class="payment-form" id="payment-form">
      <label for="name">Name</label>
      <input type="text" id="name" v-model="paymentDetails.name" placeholder="First Last" required />

      <label for="city">City</label>
      <input type="text" id="city" v-model="paymentDetails.city" placeholder="City" required />

      <label for="card-number">Card Number</label>
      <input
          type="text"
          id="card-number"
          v-model="paymentDetails.cardNumber"
          maxlength="16"
          placeholder="Card Number"
          required
      />

      <div class="card-details">
        <div>
          <label for="expiry-month">Expires</label>
          <select id="expiry-month" v-model="paymentDetails.expiryMonth" required>
            <option value="" disabled selected>Month</option>
            <option v-for="month in months" :key="month" :value="month">{{ month }}</option>
          </select>
        </div>
        <div>
          <label for="expiry-year">Year</label>
          <select id="expiry-year" v-model="paymentDetails.expiryYear" required>
            <option value="" disabled selected>Year</option>
            <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
          </select>
        </div>
        <div>
          <label for="cvc">CVC</label>
          <input
              type="text"
              id="cvc"
              v-model="paymentDetails.cvc"
              maxlength="3"
              placeholder="CVC"
              required
          />
        </div>
      </div>

      <button type="submit" class="submit-btn">Pay Now</button>
    </form>
  </div>
  </div>
  </body>
</template>

<script>
export default {
  name: "payment",
  data() {
    return {
      customer: null, // Customer details
      paymentDetails: {
        customer_email :"",
        name: "",
        city: "",
        cardNumber: "",
        expiryMonth: "",
        expiryYear: "",
        cvc: "",
      },
      months: ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
      years: Array.from({ length: 26 }, (_, i) => new Date().getFullYear() + i), // Dynamic year range
      methods: ["Credit Card", "Debit Card"], // Payment methods
      selectedMethod: "Credit Card", // Default selected method
    };
  },
  async created() {
    try {
      // Fetch customer details from session storage

      // Load stored payment details if available
      const storedPaymentDetails = JSON.parse(localStorage.getItem("paymentDetails"));
      if (storedPaymentDetails) {
        this.paymentDetails = storedPaymentDetails;
        console.log("Loaded stored payment details:", storedPaymentDetails);
      }
    } catch (error) {
      console.error("Error initializing payment page:", error);
    }
  },
  methods: {
    async submitPayment() {
      try {
        console.log("Processing payment with details:", this.paymentDetails);

        // Validate input
        if (
            !this.paymentDetails.name ||
            !this.paymentDetails.city ||
            !this.paymentDetails.cardNumber ||
            !this.paymentDetails.expiryMonth ||
            !this.paymentDetails.expiryYear ||
            !this.paymentDetails.cvc
        ) {
          alert("Please fill out all payment details.");
          return;
        }

        // Save payment details to localStorage
        this.paymentDetails.customer_email = JSON.parse(sessionStorage.getItem("customer")).email;
        console.log(this.paymentDetails.customer_email)
        localStorage.setItem("paymentDetails", JSON.stringify(this.paymentDetails));
        console.log("Payment details saved to localStorage.");

        alert("Payment successful!");

        // Reset payment details after successful submission
        this.resetPaymentDetails();
      } catch (error) {
        console.error("Payment processing error:", error.response?.data || error.message);
        alert("Payment failed. Please try again.");
      }
    },
    resetPaymentDetails() {
      setTimeout(() => {
        this.paymentDetails = {
          name: "",
          city: "",
          cardNumber: "",
          expiryMonth: "",
          expiryYear: "",
          cvc: "",
        };
        this.$router.push("/cart"); // Navigate back to the cart page
      }, 1000);
      // Optionally clear localStorage
      //localStorage.removeItem("paymentDetails");
    },
    selectMethod(method) {
      this.selectedMethod = method;
    },
  },
}
</script>

<style>
body {
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.payment-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.payment-box {
  background-color: #ffffff;
  border-radius: 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  padding: 30px;
  max-width: 10000px;
  width: 100%;
  text-align: center;
}

h1 {
  font-size: 24px;
  margin-bottom: 10px;
}

p {
  color: #666666;
  margin-bottom: 20px;
}

.payment-methods {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.method {
  flex: 1;
  padding: 10px;
  margin: 0 5px;
  color: #000000;
  border: 1px solid #cccccc;
  background-color: #f9f9f9;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
}

.method.selected {
  border: 2px solid black;
  background-color: white;
}

span {
  display: block;
}

.payment-form label {
  display: block;
  margin-bottom: 5px;
  font-size: 14px;
  text-align: left;
}

.payment-form input,
.payment-form select {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
}

input,
select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.card-details {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.card-details div {
  flex: 1;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  background-color: #000000;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #333333;
}
</style>