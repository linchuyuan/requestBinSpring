# Request Bin in Spring(JAVA)

map("/") displays basic imformation about the app

get(map("/bin")) return all bins and its url. In case of no bin created, return bad request.

post(map("/bin")) create a bin bin bucket and return its ID.

map("/bin/{id}") log the request to the bin bucket with that id. In case of failure, return bad request.

map("/bin/{id}/inspect") return all logged request in that bin bucket.




