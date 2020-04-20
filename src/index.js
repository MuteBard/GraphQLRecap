import { GraphQLServer } from 'graphql-yoga'

//Type-definitions (schema)

//contains the five scalar types example:
const typeDefs = `
    type Query {
        id: ID!
        name : String!
        age : Int!
        employed : Boolean!
        gpa : Float
    }
`
//Resolvers
const resolvers = {
    Query: {
        id() {
            return 'abc123'
        },
        name(){
            return 'MuteBard'
        },
        age(){
            return 27
        },
        employed(){
            return true
        },
        gpa(){
            return null
        }
    }
}

const server = new GraphQLServer({
    typeDefs,resolvers
})

server.start(() => {
    console.log("The GraphQL server is up")
})