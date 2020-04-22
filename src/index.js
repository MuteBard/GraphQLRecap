import { GraphQLServer } from 'graphql-yoga'

//Type-definitions (schema)

//contains the five scalar types example:

const users = [{
        id : '1',
        name : 'Carl',
        email : 'abc@gmail.com'
    },
    {
        id : '2',
        name : 'Erin',
        email : 'abc@gmail.com'
    },
    {
        id : '3',
        name : 'Carla',
        email : 'abc@gmail.com'
    }
]

const typeDefs = `
    type Query {
        users(name: String!) : [User!]!
        getBugBaseOnMonth(month : String!) : String!
        getQuarterBlockPrices : [Int!]!
        getAveragePrice(prices : [Int!]!) : Int!
        me: User!
        post: Post!
    }

    type User { 
        id : ID!
        name : String!
        email : String!
        age : Int
    }

    type Post {
        id : ID!
        title : String!
        body : String!
        published : String!
    }
`
//Resolvers
const resolvers = {
    Query: {

        users(parent, args, ctx, info){
            console.log(args.name)
            return users.filter(user => user.name.toLowerCase().includes(args.name.toLowerCase()))
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
        },

        me() {
            return {
                id : "123abc",
                name : "MuteBard",
                email : "someemail@gmail.com",
                age : 27
            }
        },
        post(){
            return{
                id : "123xyz",
                title : "I enjoyed this",
                body : "Ill about to discuss why this is great, blah blah blah",
                published : "02:12AM 20 April 2020"
            }
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
// 	me{
//     name
//     age
//   }
// }


// {
//     "data": {
//       "me": {
//         "name": "MuteBard",
//         "age": 27
//       }
//     }
//   }







// query{
//     users{
//       name
//     }
//     getBugBaseOnMonth(month : "MAR")
//     getQuarterBlockPrices
//     getAveragePrice(prices:[983,923,874,903])
//   }

  
//   {
//     "data": {
//       "users": [
//         {
//           "name": "Carl"
//         },
//         {
//           "name": "Erin"
//         },
//         {
//           "name": "Carla"
//         }
//       ],
//       "getBugBaseOnMonth": "This is the list of bugs on MAR: Bee",
//       "getQuarterBlockPrices": [
//         983,
//         923,
//         874,
//         903
//       ],
//       "getAveragePrice": 920
//     }
//   }

// query{
//     users(name: "Erin"){
//       id
//       name
//     }
//     getBugBaseOnMonth(month : "MAR")
//     getQuarterBlockPrices
//     getAveragePrice(prices:[983,923,874,903])
//   }


// {
//     "data": {
//       "users": [
//         {
//           "id": "2",
//           "name": "Erin"
//         }
//       ],
//       "getBugBaseOnMonth": "This is the list of bugs on MAR: Bee",
//       "getQuarterBlockPrices": [
//         983,
//         923,
//         874,
//         903
//       ],
//       "getAveragePrice": 920
//     }
//   }