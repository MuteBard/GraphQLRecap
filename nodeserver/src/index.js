import { GraphQLServer, PubSub} from 'graphql-yoga'

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

const pubsub = new PubSub()

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
    type Subscription{
        count : Int!
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
            letnewUser = {
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
    },

    Subscription :  {
        count : {
            subscribe(parent, args, {pubsub}, info){
                let count = 0
                setInterval(() => {
                    count++
                    pubsub.publish('count', {
                        count
                    })
                },1000)
                return pubsub.asyncIterator('count')
            }
        }
    }
}

const context = {
    pubsub
}

const server = new GraphQLServer({
    typeDefs,resolvers,context
})

server.start(() => {
    console.log("The GraphQL server is up at http://localhost:4000")
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






