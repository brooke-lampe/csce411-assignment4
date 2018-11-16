function idIndex(a,id) {
    for (var i=0;i<a.length;i++) {
        if (a[i].id == id) return i;}
    return null;
}
var xmlhttp = new XMLHttpRequest();
$('#search').off('click').on('click', function (e) {
    var url = "http://localhost:11002/db/data/transaction/commit";
    xmlhttp.open("post", url, true);
    xmlhttp.setRequestHeader("Accept", "application/json;charset=UTF-8");
    xmlhttp.setRequestHeader("Content-type", "application/json");

    var query = $('#query').val();
    var query_com = {
        "statements": [
            {
                "statement": query,
                "resultDataContents": ["graph"]
            }
        ]
    };
    var query_str = JSON.stringify(query_com);
    xmlhttp.send(query_str);
    return false;
});
xmlhttp.onreadystatechange = function () {
    console.log(xmlhttp.responseText);
    if(xmlhttp.responseText !== ""){
        var res = eval("("+xmlhttp.responseText+")");
        var nodes=[], links=[];
        res.results[0].data.forEach(function (row) {
            row.graph.nodes.forEach(function (n) {
                if (idIndex(nodes,n.id) == null)
                    nodes.push({id:n.id,label:n.labels[0],title:n.properties.name});
            });
            links = links.concat( row.graph.relationships.map(function(r) {
                return {source:idIndex(nodes,r.startNode),target:idIndex(nodes,r.endNode),type:r.type};
            }));
        });
        console.log(nodes.length, links.length);
        makeGraph(nodes, links);
        xmlhttp.abort();
    }
};


function makeGraph(nodes, links) {
    var width = 800, height = 800;
    var force = d3.layout.force()
      .charge(-200)
      .linkDistance(30)
      .size([width, height]);

    var svg = d3.select("#graph").append("svg")
      .attr("width", "100%").attr("height", "100%")
      .attr("pointer-events", "all");

    force.nodes(nodes).links(links).start();

    var link = svg.selectAll(".link")
      .data(links).enter()
      .append("line").attr("class", "link");

    var node = svg.selectAll(".node")
      .data(nodes).enter()
      .append("circle")
      .attr("class", d => { return "node " + d.label })
      .attr("r", 5)
      .call(force.drag);

    node.append("title").text(d => { return d.source; });

    node.on("mouseover", function(d) {
      node.style("fill", (o) => {
        if (d == o) return "magenta";
      });
      node.style("stroke", (o) => {
        if (d == o) return "magenta";
      });
    });

    node.on("mouseout", function(d, i) {
      node.style("stroke", "#222");
      node.style("fill", "#222");
    });

    force.on("tick", () => {
      link.attr("x1", d => { return d.source.x; })
          .attr("y1", d => { return d.source.y; })
          .attr("x2", d => { return d.target.x; })
          .attr("y2", d => { return d.target.y; });

      node.attr("cx", d => { return d.x })
          .attr("cy", d => { return d.y;});
    });

  }