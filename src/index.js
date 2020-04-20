import { GraphQLServer } from 'graphql-yoga'

//Type-definitions (schema)

//contains the five scalar types example:
const typeDefs = `
    type Query {
        me: User!
        post: Post!
    }

    type User { 
        id : ID!
        name : String!
        email : String!
        age : Int!
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