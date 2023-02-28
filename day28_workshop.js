use bgg;

db.comment.findOne();

db.comment.aggregate([
    {
        $match: {gid: 5}    }, 
    {
        $project: {"c_id": 1}
    }
])

db.comment.aggregate([
    {
        $group: {_id: "$gid", count: {$sum: 1}, ratings: {$push: "$rating"}}    },
    {
        $sort: {count: -1}    },
    {
        $limit: 5
    }
])

db.comment.aggregate([
    {
        $match: {gid: 822}    },
    {
        $bucket: {groupBy: "$rating", boundaries: [0, 3, 5, 8, 10], default: "others", output: {"count": {$sum: 1}, comments: {$push: "$c_text"}}}    }
])

db.game.findOne()

db.game.aggregate([
    {
        $match: {gid: 822}    },
    {
        $lookup: {from: "comment", foreignField: "gid", localField: "gid", as: "comment"}    },
    {
        $unwind: "$comment"
            },
    {
        $sort: {"comment.rating": -1}    },
    {
        $limit: 5    }
])

db.game.aggregate([
    {
        $match: {gid: 822}
    },
    {
        $lookup: {from: "comment", foreignField: "gid", localField: "gid", as: "reviews"}
    },
    {
        $project: {}    }
])

db.game.aggregate([
    {
        $match: {gid: 822}
    },

    {
        $lookup: {from: "comment", foreignField: "gid", localField: "gid", as: "reviews"}
    },
    {
        $unwind: "$reviews"    },
    {
        $project: {"gid": 1, "reviews.ref": {$concat: ["/review/", "$reviews.c_id"]}}
            },
    {
        $group: {"_id": "$gid", reviews: {$push: "$reviews"}}    }
])

db.comment.aggregate([
    {
        $match: {gid: 822}    },
    {
        $group: {"_id": "$gid", "average": {$avg: "$rating"}}    }
])

db.comment.aggregate([

    {
        $sort: {"rating": -1}    },
    {
        $group: {"_id": "$gid", "comments": {$push: "$c_text"}}    }
])


db.comment.aggregate([
    {
        $limit: 250
    },
   
    {
        $sort: {"rating": -1}
    },

    {
        $group: {_id: "$gid",
        "rating": {$first: "$rating"},
        "user": {$first: "$user"},
        "comment": {$first: "$c_text"},
        "review_id": {$first: "$c_id"}
        
        }
    },
    {
        $sort: {"_id": 1}
    }
])

// THE CORRECT QUERY BELOW

db.game.aggregate([

    {
        $lookup: {from: "comment", foreignField: "gid", localField: "gid",
            pipeline: [
                {$sort: {"rating": 1}},
                {$limit: 1}            ],
            as: "review"}
    },
    {
        $unwind: "$review"
    },

    {
        $project: {"gid": 1, "name": 1, "rating":"$review.rating", "user":"$review.user", "c_text":"$review.c_text", "c_id":"$review.c_id"}
    }
])






























