import ApolloBoost, { gql } from 'apollo-boost'

const client = new ApolloBoost({
    uri : 'http://localhost:4000'
})


const MutationExample = gql`
mutation {
    catchBug {
        username
        pocket {
            bug {
                bugId
                name
                bells
                availability
            }
        }
    }
}  
`
const QueryExample = gql`
query{
    getBugBaseOnMonth(month : "JUL")    
}
`


client.mutate({
    mutation : MutationExample
}).then(resp => {
    console.log(resp.data)
})


client.query({
    query : QueryExample
}).then(resp => {
    console.log(resp.data)
})



