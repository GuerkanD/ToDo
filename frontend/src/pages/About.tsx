export default function About() {
    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header bg-primary text-white">
                    <h2>About This ToDo Application</h2>
                </div>
                <div className="card-body">
                    <p className="card-text">
                        Stay organized and boost your productivity with this intuitive ToDo application. Designed for
                        simplicity and efficiency, it offers the following features:
                    </p>
                    <ul className="list-group list-group-flush">
                        <li className="list-group-item">
                            <strong>Create Tasks:</strong> Easily add tasks to your to-do list to keep track of
                            everything you need to accomplish.
                        </li>
                        <li className="list-group-item">
                            <strong>Create Categories:</strong> Group your tasks into categories for better organization
                            and clarity.
                        </li>
                        <li className="list-group-item">
                            <strong>Edit and Delete Tasks:</strong> Update or remove tasks with ease to ensure your list
                            stays relevant and up-to-date.
                        </li>
                    </ul>
                    <p className="mt-3">
                        Whether you're managing daily chores or planning long-term projects, this app is here to help
                        you stay on top of your tasks effortlessly.
                    </p>
                </div>
                <div className="row justify-content-center">
                    <button className="btn btn-primary m-2 col-4">Register</button>
                    <button className="btn btn-primary m-2 col-4">Login</button>
                </div>
            </div>
        </div>
    )
}