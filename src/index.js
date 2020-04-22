// import { GraphQLServer } from 'graphql-yoga'

// //Type-definitions (schema)

// //contains the five scalar types example:

// const users = [{
//         id : '1',
//         name : 'Carl',
//         email : 'abc@gmail.com'
//     },
//     {
//         id : '2',
//         name : 'Erin',
//         email : 'abc@gmail.com'
//     },
//     {
//         id : '3',
//         name : 'Carla',
//         email : 'abc@gmail.com'
//     }
// ]
// const typeDefs = `
//     type Query {
//         users(name: String!) : [User!]!
//         getBugBaseOnMonth(month : String!) : String!
//         getQuarterBlockPrices : [Int!]!
//         getAveragePrice(prices : [Int!]!) : Int!
//         me: User!
//         post: Post!
//     }

//     type User { 
//         id : ID!
//         name : String!
//         email : String!
//         age : Int!
//     }

//     type Post {
//         id : ID!
//         title : String!
//         body : String!
//         published : String!
//     }
// `
// //Resolvers
// const resolvers = {
//     Query: {

//         users(parent, args, ctx, info){
//             console.log(args.name)
//             return users.filter(user => user.name.toLowerCase().includes(args.name.toLowerCase()))
//         },

//         getBugBaseOnMonth(parent, args, ctx, info){
//             console.log(args)
//             return `This is the list of bugs on ${args.month}: Bee`
//         },

//         getQuarterBlockPrices(parent, args, ctx, info){
//             return [983,923,874,903]
//         },

//         getAveragePrice(parent, args, ctx, info){
//             if(args.prices.length == 0) return 0
//             else return Math.floor((args.prices.reduce((sum, elem) => elem + sum))/args.prices.length)
//         },

//         me() {
//             return {
//                 id : "123abc",
//                 name : "MuteBard",
//                 email : "someemail@gmail.com",
//                 age : 27
//             }
//         },
//         post(){
//             return{
//                 id : "123xyz",
//                 title : "I enjoyed this",
//                 body : "Ill about to discuss why this is great, blah blah blah",
//                 published : "02:12AM 20 April 2020"
//             }
//         }
//     }
// }

// const server = new GraphQLServer({
//     typeDefs,resolvers
// })

// server.start(() => {
//     console.log("The GraphQL server is up")
// })

// // query{
// // 	me{
// //     name
// //     age
// //   }
// // }


// // {
// //     "data": {
// //       "me": {
// //         "name": "MuteBard",
// //         "age": 27
// //       }
// //     }
// //   }

// // query{
// //     users{
// //       name
// //     }
// //     getBugBaseOnMonth(month : "MAR")
// //     getQuarterBlockPrices
// //     getAveragePrice(prices:[983,923,874,903])
// //   }

  
// //   {
// //     "data": {
// //       "users": [
// //         {
// //           "name": "Carl"
// //         },
// //         {
// //           "name": "Erin"
// //         },
// //         {
// //           "name": "Carla"
// //         }
// //       ],
// //       "getBugBaseOnMonth": "This is the list of bugs on MAR: Bee",
// //       "getQuarterBlockPrices": [
// //         983,
// //         923,
// //         874,
// //         903
// //       ],
// //       "getAveragePrice": 920
// //     }
// //   }

// // query{
// //     users(name: "Erin"){
// //       id
// //       name
// //     }
// //     getBugBaseOnMonth(month : "MAR")
// //     getQuarterBlockPrices
// //     getAveragePrice(prices:[983,923,874,903])
// //   }


// // {
// //     "data": {
// //       "users": [
// //         {
// //           "id": "2",
// //           "name": "Erin"
// //         }
// //       ],
// //       "getBugBaseOnMonth": "This is the list of bugs on MAR: Bee",
// //       "getQuarterBlockPrices": [
// //         983,
// //         923,
// //         874,
// //         903
// //       ],
// //       "getAveragePrice": 920
// //     }
// //   }

import { GraphQLServer } from 'graphql-yoga'

//Type-definitions (schema)

let AgriasButterfly = {
    id : 7,
    bugId : "B7",
    name : "Agrias Butterfly",
    bells : 3000,
    availability : ["JUN","JUL","AUG","SEP"],
    rarity : 4,
    img : "Agrias_Butterfly_HHD_Icon.png"
}

let Crawfish = {
    id : 11,
    fishId : "F11",
    name : "Crawfish",
    bells : 200,
    availability : ["APR","MAY","JUN","JUL","AUG","SEP"],
    rarity : 2,
    img : "Crawfish_HHD_Icon.png"
}

let pocket = {
    bug : [],
    fish : []
} 

let user = {
    id : 1,
    username : "MuteBard",
    fishingPoleLvl : 0,
    bugNetLvl : 0,
    bells : 0,
    pocket,
    liveTurnips : null,
    turnipTransactionHistory : [],
    img : "mutebard.jpg",
}

const typeDefs = `
    type Query {
        getUser : User!
        getBugBaseOnMonth(month : String!) : String!
        getQuarterBlockPrices : [Int!]!
        getAveragePrice(prices : [Int!]!) : Int!
    }
    type Mutation{
        catchBug : User!
        catchFish : User!
        hardCreateUser(data : addUser): User!
    }

    input addUser{
        username : String!,
        img : String!
    }

    type User{
        id : Int!,
        username : String!,
        fishingPoleLvl : Int!,
        bugNetLvl : Int!,
        bells : Int!,
        pocket : Pocket!,
        liveTurnips : TurnipTransaction,
        turnipTransactionHistory : [TurnipTransaction]!,
        img : String!,
    }


    type Pocket{
        bug : [Bug]!,
		fish : [Fish]!
    }

    type TurnipTransaction{
        business: String!,
        quantity: Int!,
        marketPrice : Int!,
        totalBells: Int!,
        netGainLossAsBells : Int,
        netGainLossAsPercentage: Int
    }

    type Fish {
        id : Int!,
		fishId : String!,
		name : String!,
		bells : Int!,
		availability : [String!]!,
		rarity : Int!,
		img : String!
    }

    type Bug {
        id : Int!,
		bugId : String!,
		name : String!,
		bells : Int!,
		availability : [String!]!,
		rarity : Int!,
		img : String!
    }
`
//Resolvers
const resolvers = {
    Mutation:{
        catchBug(){
            user.pocket.bug.push(AgriasButterfly)
            return user
        },
        catchFish(){
            user.pocket.fish.push(Crawfish)
            return user
        },
        hardCreateUser(parent, args, ctx, info){
            let newUser = {
                id : 1,
                fishingPoleLvl : 0,
                bugNetLvl : 0,
                bells : 0,
                pocket,
                liveTurnips : null,
                turnipTransactionHistory : [],
                ...args.data
            }
            return newUser
        }
    },
    Query: {

        getUser(){
            return user
            
        },

        getBugBaseOnMonth(parent, args, ctx, info){
            console.log(args)
            return `This is the list of bugs on ${args.month}: Bee`
        },

        getQuarterBlockPrices(parent, args, ctx, info){
            return [983,923,874,903]
        },

        getAveragePrice(parent, args, ctx, info){
            if(args.prices.length == 0) return 0
            else return Math.floor((args.prices.reduce((sum, elem) => elem + sum))/args.prices.length)
        }
    }
}

const server = new GraphQLServer({
    typeDefs,resolvers
})

server.start(() => {
    console.log("The GraphQL server is up")
})

// query{
//     getUser{
//   username
//   pocket{
//     bug{
//         bugId
//          name
//         rarity
//       bells
//     }
//   }
// }
// }

// {
//     "data": {
//       "getUser": {
//         "username": "MuteBard",
//         "pocket": {
//           "bug": [
//             {
//               "bugId": "B7",
//               "name": "Agrias Butterfly",
//               "rarity": 4,
//               "bells": 3000
//             }
//           ]
//         }
//       }
//     }
//   }






