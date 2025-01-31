<template>
  <div v-if="game">
    <h1>{{ game.title }}</h1>
    <p>{{ game.description }}</p>
    <p>Genre: {{ game.genre }}</p>
    <p>Price: ${{ game.price }}</p>
    <p>Stock: {{ game.stock }}</p>
    <button @click="addThisToWishList">Add to Wishlist</button>
    <button @click="addThisToCart">Add to Cart</button>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const axiosClient = axios.create({
  baseURL: "http://localhost:8080", // Backend API base URL
  headers: {
    'Content-Type': 'application/json'
  },
});

export default {
  name: 'GameDetails',
  setup() {
    const route = useRoute();
    const game = ref(null);
    const isCustomer = sessionStorage.getItem('customer') !== null;

    const fetchGameDetails = async () => {
      try {
        const response = await axiosClient.get(`/api/games/${route.params.id}/details`);
        // Map only the required fields to the game object
        const { gameId, title, description, genre, price, stock } = response.data;
        game.value = { gameId, title, description, genre, price, stock };
      } catch (error) {
        console.error('Error fetching game details:', error);
      }
    };

    const addThisToWishList = async () => {
      if(isCustomer){
        try{
          let user = sessionStorage.getItem('customer')
          await axiosClient.put(`/customers/addWishList/${game.value.gameId}`, user)
          showNotification()
        }catch(err){
          console.error('Error adding wishlist:', err);
        }
      }
      // Add logic for adding the game to the wishlist
    };

    const addThisToCart = async () => {
      // Add logic for adding the game to the cart
      if(isCustomer){
        try{
          let user = sessionStorage.getItem('customer')
          const gameCopyResponse = await axiosClient.post(`/game-copy`, game.value);
          console.log('GameCopy created:', gameCopyResponse.data);
          await axiosClient.put(`/customers/addCart/${gameCopyResponse.data.gameCopyId}`, user)
        }catch(err){
          console.error('Error adding cart:', err);
        }
      }
    };

    // TODO: add addReview function in the game detail page

    onMounted(fetchGameDetails);

    return { game, addThisToWishList, addThisToCart };
  },
};
</script>
