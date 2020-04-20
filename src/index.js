import { GraphQLServer } from 'graphql-yoga'

//Type-definitions (schema)
const typeDefs = `
    type Query {
        hello : String!
        name : String!
    }
`
//Resolvers
const resolvers = {
    Query: {
        hello() {
            return 'this is my first Query'
        },
        name(){
            return 'MuteBard'
        }
    }
}

const server = new GraphQLServer({
    typeDefs,resolvers
})

server.start(() => {
    console.log("The GraphQL server is up")
})