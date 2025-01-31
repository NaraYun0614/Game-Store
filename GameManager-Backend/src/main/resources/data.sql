INSERT INTO owner(email, name, password)
SELECT 'manager@mail.com', 'manager', 'c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646'
    WHERE NOT EXISTS (SELECT 1 FROM owner WHERE email = 'manager@mail.com');
INSERT INTO customer(email, name, password)
SELECT 'customer@mail.com', 'customer', 'c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646'
    WHERE NOT EXISTS (SELECT 1 FROM owner WHERE email = 'customer@mail.com');
INSERT INTO category(name,description)
SELECT 'Action', 'An action game is a video game genre that emphasizes physical challenges, including hand-eye coordination and reaction time.'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Action');
INSERT INTO category(name,description)
SELECT 'Puzzle', 'Puzzle games focus on completion, which requires players to solve a logic puzzle or navigate a challenge to progress to the next, more difficult challenge.'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Puzzle');
INSERT INTO category(name,description)
SELECT 'Strategy', 'A strategic game is a model of interaction in which each player chooses an action not having been informed of the other players.'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Strategy');
INSERT INTO category(name,description)
SELECT 'Racing', 'Racing games are a video game genre where players race to the finish line in a vehicle, either against other drivers or against the clock.'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Racing');
INSERT INTO category(name,description)
SELECT 'Sandbox','A sandbox game provides players a great degree of creativity to interact with, usually without any predetermined goal, or with a goal that the players set for themselves.'
    WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Sandbox');
INSERT INTO game(average_rating, description, game_status, genre, popularity, price, request_status, stock, title, category)
SELECT 0, 'Embark on an odyssey for the Lost Ark in a vast, vibrant world: explore new lands, seek out lost treasures, and test yourself in thrilling action combat in this action-packed free-to-play RPG.', 1, 'action', 0, 33.99, 0, 100, 'Lost Ark', (SELECT category_id FROM category WHERE name = 'Action')
    WHERE NOT EXISTS (SELECT 1 FROM game WHERE game_id = 17);
INSERT INTO game(average_rating, description, game_status, genre, popularity, price, request_status, stock, title, category)
SELECT 0, 'Open your own local game store. Stock shelves with the latest booster packs, or crack them and collect the cards for yourself. Set your own prices, hire staff, host events, and expand your card shop.', 1, 'strategy', 0, 16.99, 0, 100, 'TCG Card Shop Simulator',(SELECT category_id FROM category WHERE name = 'Strategy')
    WHERE NOT EXISTS (SELECT 1 FROM game WHERE game_id = 18);
INSERT INTO game(average_rating, description, game_status, genre, popularity, price, request_status, stock, title, category)
SELECT 0, 'Grand Theft Auto V for PC offers players the option to explore the award-winning world of Los Santos and Blaine County in resolutions of up to 4k and beyond, as well as the chance to experience the game running at 60 frames per second.', 1, 'racing', 0,29.98, 0, 100,'Grand Theft Auto V', (SELECT category_id FROM category WHERE name = 'Racing')
    WHERE NOT EXISTS (SELECT 1 FROM game WHERE game_id = 19);
INSERT INTO game(average_rating, description, game_status, genre, popularity, price, request_status, stock, title, category, image_url)
SELECT 0, 'Overcooked returns with a brand-new helping of chaotic cooking action! Journey back to the Onion Kingdom and assemble your team of chefs in classic couch co-op or online play for up to four players. Hold onto your aprons… it’s time to save the world again!', 1, 'action', 0,28.99, 0, 100,'Overcooked! 2', (SELECT category_id FROM category WHERE name = 'Action'), 'https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/728880/header.jpg?t=1729768553'
    WHERE NOT EXISTS (SELECT 1 FROM game WHERE game_id = 20);
INSERT INTO game(average_rating, description, game_status, genre, popularity, price, request_status, stock, title, category, image_url)
SELECT 0, 'Little Nightmares II is the second game in a series that has thrilled and captivated millions of players worldwide since 2017. Now it’s your turn to try to survive this critically acclaimed entry in the most charming horror series ever made.', 1, 'adventure', 0,28.99, 0, 100,'Little Nightmares II', (SELECT category_id FROM category WHERE name = 'Adventure'), 'https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/860510/header.jpg?t=1730127763'
    WHERE NOT EXISTS (SELECT 1 FROM game WHERE game_id = 21);
